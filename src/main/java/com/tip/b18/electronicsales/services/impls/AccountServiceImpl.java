package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.dto.*;
import com.tip.b18.electronicsales.entities.Account;
import com.tip.b18.electronicsales.exceptions.*;
import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.mappers.AccountMapper;
import com.tip.b18.electronicsales.repositories.AccountRepository;
import com.tip.b18.electronicsales.services.AccountService;
import com.tip.b18.electronicsales.services.PasswordService;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Objects;
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
    public void registerAccount(AccountRegisterDTO accountRegisterDTO) {
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
        account.setGender(null);

        accountRepository.save(account);
    }

    @Override
    public CustomPage<AccountsDTO> viewAccounts(String search, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        org.springframework.data.domain.Page<Account> accounts = accountRepository.findByUserNameOrPhoneNumber(search, pageable);

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
        String phoneNumber = accountUpdateDTO.getPhoneNumber();
        LocalDate dateOfBirth = accountUpdateDTO.getDateOfBirth();
        Boolean gender = accountUpdateDTO.getGender();
        String avatarUrl = accountUpdateDTO.getAvatarUrl();

        boolean isChange = false;

        if(fullName != null && !fullName.equals(account.getFullName())){
            account.setFullName(fullName);
            isChange = true;
        }
        if(!Objects.equals(address, account.getAddress())){
            account.setAddress(address);
            isChange = true;
        }
        if(!Objects.equals(email, account.getEmail())){
            if(email != null && accountRepository.existsByEmail(email)){
                throw new AlreadyExistsException(MessageConstant.ERROR_EMAIL_EXISTS);
            }
            account.setEmail(email);
            isChange = true;
        }
        if(!Objects.equals(phoneNumber, account.getPhoneNumber())){
            if(phoneNumber != null && accountRepository.existsByPhoneNumber(phoneNumber)){
                throw new AlreadyExistsException(MessageConstant.ERROR_PHONE_NUMBER_EXISTS);
            }
            account.setPhoneNumber(phoneNumber);
            isChange = true;
        }
        if(!Objects.equals(dateOfBirth, account.getDateOfBirth())){
            account.setDateOfBirth(dateOfBirth);
            isChange = true;
        }
        if(!Objects.equals(gender, account.getGender())){
            account.setGender(gender);
            isChange = true;
        }
        if(!Objects.equals(avatarUrl, account.getAvatarUrl())){
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
        try {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ACCOUNT));
            accountRepository.delete(account);
        }catch (DataIntegrityViolationException e){
            throw new IntegrityConstraintViolationException(MessageConstant.ERROR_ACCOUNT_HAS_ORDERS);
        }
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

    @Override
    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ACCOUNT));
    }
}
