package com.tip.b18.electronicsales.controllers;

import com.tip.b18.electronicsales.dto.*;
import com.tip.b18.electronicsales.exceptions.NotFoundException;
import com.tip.b18.electronicsales.exceptions.CredentialsException;
import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.services.AccountService;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseDTO<CustomPage<AccountsDTO>> viewAccounts(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                             @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                             @RequestParam(name = "limit", required = false, defaultValue = "5") int limit){
        CustomPage<AccountsDTO> accountsDTOS = accountService.viewAccounts(search, page, limit);

        ResponseDTO<CustomPage<AccountsDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setData(accountsDTOS);

        return responseDTO;
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseDTO<AccountPersonalDTO> viewPersonalAccount(@RequestParam(name = "id", required = false) @Valid UUID id){
        AccountPersonalDTO accountPersonalDTO = accountService.viewPersonalAccount(id);

        ResponseDTO<AccountPersonalDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setData(accountPersonalDTO);

        return responseDTO;
    }

    @PutMapping("/personal")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseDTO<AccountUpdateDTO> updatePersonalAccount(@RequestBody @Valid AccountUpdateDTO accountDTORequest){
        AccountUpdateDTO accountUpdateDTO = accountService.updatePersonalAccount(accountDTORequest);

        ResponseDTO<AccountUpdateDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setMessage(MessageConstant.SUCCESS_UPDATE);
        responseDTO.setData(accountUpdateDTO);

        return responseDTO;
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseDTO<AccountUpdateDTO> deleteAccount(@RequestParam(name = "id") @Valid UUID id){
        accountService.deleteAccount(id);

        ResponseDTO<AccountUpdateDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setMessage(MessageConstant.SUCCESS_DELETE);

        return responseDTO;
    }

    @PutMapping("update-password")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseDTO<AccountUpdateDTO> changePasswordAccount(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        accountService.changePassword(updatePasswordDTO);

        ResponseDTO<AccountUpdateDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setMessage(MessageConstant.SUCCESS_CHANGE);

        return responseDTO;
    }
}
