package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.dto.*;
import com.tip.b18.electronicsales.entities.Account;
import com.tip.b18.electronicsales.exceptions.AlreadyExistsException;
import com.tip.b18.electronicsales.exceptions.InvalidPasswordException;
import com.tip.b18.electronicsales.exceptions.NotFoundException;
import com.tip.b18.electronicsales.exceptions.CredentialsException;
import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.mappers.AccountMapper;
import com.tip.b18.electronicsales.repositories.AccountRepository;
import com.tip.b18.electronicsales.services.AccountService;
import com.tip.b18.electronicsales.services.PasswordService;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordService passwordService;
    private final AccountMapper accountMapper;

    @Override
    public AccountDTO loginAccount(AccountLoginDTO accountLoginDTO) {
        Account account = accountRepository.findByUserName(accountLoginDTO.getUserName())
                .orElseThrow(() -> new CredentialsException(MessageConstant.ERROR_INVALID_CREDENTIALS));

        boolean checkPassword = passwordService.matches(accountLoginDTO.getPassword(), account.getPassword());
        if (!checkPassword) {
            throw new CredentialsException(MessageConstant.ERROR_INVALID_CREDENTIALS);
        }

        return accountMapper.toDTO(account);
    }

    @Override
    public AccountDTO registerAccount(AccountRegisterDTO accountRegisterDTO) {
        accountRepository.findByUserName(accountRegisterDTO.getUserName())
                .ifPresent(account -> {
                    throw new AlreadyExistsException(MessageConstant.ERROR_ACCOUNT_EXISTS);
                });

        String password = passwordService.encryptPassword(accountRegisterDTO.getPassword());

        Account account = new Account();
        account.setFullName(accountRegisterDTO.getFullName());
        account.setUserName(accountRegisterDTO.getUserName());
        account.setPassword(password);
        account.setRole(false);

        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public CustomPage<AccountsDTO> viewAccounts(String search, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        org.springframework.data.domain.Page<Account> accounts = accountRepository.findByUserNameOrNumberPhone(search, pageable);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalPages(accounts.getTotalPages());
        pageInfoDTO.setTotalElements(accounts.getTotalElements());

        CustomPage<AccountsDTO> accountListDTO = new CustomPage<>();
        accountListDTO.setPageInfo(pageInfoDTO);
        accountListDTO.setItems(accountMapper.toDTOList(accounts));

        return accountListDTO;
    }

    @Override
    public AccountPersonalDTO viewPersonalAccount(UUID id) {
        UUID userId = SecurityUtil.getAuthenticatedUserId(id);
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ACCOUNT));
        account.setId(null);

        return accountMapper.toAccountPersonalDto(account);
    }

    @Override
    public AccountUpdateDTO updatePersonalAccount(AccountUpdateDTO accountUpdateDTO) {
        UUID id = SecurityUtil.getAuthenticatedUserId();
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ACCOUNT);
        }

        Account account = optionalAccount.get();

        String fullName = accountUpdateDTO.getFullName();
        String address = accountUpdateDTO.getAddress();
        String email = accountUpdateDTO.getEmail();
        String numberPhone = accountUpdateDTO.getNumberPhone();
        LocalDate birthDay = accountUpdateDTO.getBirthDay();
        boolean gender = accountUpdateDTO.isGender();
        String avatarUrl = accountUpdateDTO.getAvatarUrl();

        boolean isChange = false;

        if(fullName != null && !fullName.equals(account.getFullName())){
            account.setFullName(fullName);
            isChange = true;
        }
        if(address != null && !address.equals(account.getAddress())){
            account.setAddress(address);
            isChange = true;
        }
        if(email != null && !email.equals(account.getEmail())){
            if(accountRepository.existsByEmail(email)){
                throw new AlreadyExistsException(MessageConstant.ERROR_EMAIL_EXISTS);
            }
            account.setEmail(email);
            isChange = true;
        }
        if(numberPhone != null && !numberPhone.equals(account.getNumberPhone())){
            if(accountRepository.existsByNumberPhone(numberPhone)){
                throw new AlreadyExistsException(MessageConstant.ERROR_NUMBERPHONE_EXISTS);
            }
            account.setNumberPhone(numberPhone);
            isChange = true;
        }
        if(birthDay != null && !birthDay.equals((account.getBirthDay()))){
            account.setBirthDay(birthDay);
            isChange = true;
        }
        if(gender != account.isGender()){
            account.setGender(gender);
            isChange = true;
        }
        if(avatarUrl != null && !avatarUrl.equals(account.getAvatarUrl())){
            account.setAvatarUrl(avatarUrl);
            isChange = true;
        }

        if(isChange){
            return accountMapper.toAccountUpdateDto(accountRepository.save(account));
        }
        return accountUpdateDTO;
    }

    @Override
    public void deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ACCOUNT));

        accountRepository.delete(account);
    }

    @Override
    public void changePassword(UpdatePasswordDTO updatePasswordDTO) {
        UUID id = SecurityUtil.getAuthenticatedUserId();
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ACCOUNT);
        }

        Account account = optionalAccount.get();

        String currentPassword = account.getPassword();
        String oldPassword = updatePasswordDTO.getOldPassword();
        String newPassword = updatePasswordDTO.getNewPassword();

        if(oldPassword != null && newPassword != null){
            if(!passwordService.matches(oldPassword, currentPassword)){
                throw new InvalidPasswordException(MessageConstant.ERROR_INVALID_PASSWORD);
            }else if(passwordService.matches(newPassword, currentPassword)){
                return;
            }
            account.setPassword(passwordService.encryptPassword(newPassword));
            accountRepository.save(account);
        }
    }
}
