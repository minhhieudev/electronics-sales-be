package com.tip.b18.electronicsales.mappers;

import com.tip.b18.electronicsales.dto.AccountDTO;
import com.tip.b18.electronicsales.dto.AccountRegisterDTO;
import com.tip.b18.electronicsales.entities.Account;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO(Account account);
    default Account toAccount(AccountRegisterDTO accountRegisterDTO, String password){
        Account account = new Account();
        account.setFullName(accountRegisterDTO.getFullName());
        account.setUserName(accountRegisterDTO.getUserName());
        account.setPassword(password);
        account.setRole(false);
        account.setGender(null);
        return account;
    }
    default List<AccountDTO> toDTOList(Page<Account> accounts){
        return accounts
                .stream()
                .map(account -> AccountDTO
                        .builder()
                        .id(account.getId())
                        .fullName(account.getFullName())
                        .userName(account.getUserName())
                        .role(account.isRole())
                        .gender(account.getGender())
                        .phoneNumber(account.getPhoneNumber())
                        .build())
                .toList();
    }

    default AccountDTO toAccountPersonalDTO(Account account){
            return AccountDTO
                    .builder()
                    .fullName(account.getFullName())
                    .userName(account.getUserName())
                    .email(account.getEmail())
                    .dateOfBirth(account.getDateOfBirth())
                    .phoneNumber(account.getPhoneNumber())
                    .address(account.getAddress())
                    .role(account.isRole())
                    .address(account.getAddress())
                    .build();
    }

    default AccountDTO toAccountUpdateDto(Account account, int totalQuantity){
        return AccountDTO
                .builder()
                .fullName(account.getFullName())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .dateOfBirth(account.getDateOfBirth())
                .address(account.getAddress())
                .gender(account.getGender())
                .avatarUrl(account.getAvatarUrl())
                .totalQuantity(totalQuantity)
                .build();
    }
}
