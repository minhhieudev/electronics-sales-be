package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.*;

import java.util.UUID;

public interface AccountService {
    AccountDTO loginAccount(AccountLoginDTO accountLoginDTO);
    AccountDTO registerAccount(AccountRegisterDTO accountRegisterDTO);
    CustomPage<AccountsDTO> viewAccounts(String search, int page, int limit);
    AccountPersonalDTO viewPersonalAccount(UUID id);
    AccountUpdateDTO updatePersonalAccount(AccountUpdateDTO accountUpdateDTO);
    void deleteAccount(UUID id);
    void changePassword(UpdatePasswordDTO updatePasswordDTO);
}
