package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.*;
import com.tip.b18.electronicsales.entities.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountService {
    AccountDTO loginAccount(AccountLoginDTO accountLoginDTO);
    void registerAccount(AccountRegisterDTO accountRegisterDTO);
    CustomPage<AccountsDTO> viewAccounts(String search, int page, int limit);
    AccountPersonalDTO viewPersonalAccount(UUID id);
    AccountUpdateDTO updatePersonalAccount(AccountUpdateDTO accountUpdateDTO);
    void deleteAccount(UUID id);
    void changePassword(UpdatePasswordDTO updatePasswordDTO);
    Account findById(UUID id);
    int getQuantityNewCustomers(LocalDateTime startDay, LocalDateTime endDay);
}
