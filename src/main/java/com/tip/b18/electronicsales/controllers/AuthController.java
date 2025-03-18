package com.tip.b18.electronicsales.controllers;

import com.tip.b18.electronicsales.dto.AccountDTO;
import com.tip.b18.electronicsales.dto.AccountLoginDTO;
import com.tip.b18.electronicsales.dto.AccountRegisterDTO;
import com.tip.b18.electronicsales.dto.ResponseDTO;
import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.services.AccountService;
import com.tip.b18.electronicsales.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseDTO<AccountDTO> loginAccount(@RequestBody @Valid AccountLoginDTO accountLoginDTO){
        AccountDTO account = accountService.loginAccount(accountLoginDTO);

        String token = jwtService.generateToken(account.getUserName(), account.getId(), account.isRole());

        ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setMessage(MessageConstant.SUCCESS_ACCOUNT_LOGGED_IN);
        responseDTO.setToken(token);
        responseDTO.setData(account);
        return responseDTO;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<AccountDTO>> registerAccount(@RequestBody @Valid AccountRegisterDTO accountRegisterDTO){
        AccountDTO account = accountService.registerAccount(accountRegisterDTO);

        ResponseDTO<AccountDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setMessage(MessageConstant.SUCCESS_ACCOUNT_REGISTERED);
        responseDTO.setData(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
