package com.tip.b18.electronicsales.mappers;

import com.tip.b18.electronicsales.dto.AccountDTO;
import com.tip.b18.electronicsales.dto.AccountPersonalDTO;
import com.tip.b18.electronicsales.dto.AccountUpdateDTO;
import com.tip.b18.electronicsales.dto.AccountsDTO;
import com.tip.b18.electronicsales.entities.Account;
import org.mapstruct.Mapper;

import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO(Account account);
    List<AccountsDTO> toDTOList(Page<Account> accounts);
    AccountPersonalDTO toAccountPersonalDto(Account account);
    default AccountUpdateDTO toAccountUpdateDto(Account account, int totalQuantity){
        return AccountUpdateDTO
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
