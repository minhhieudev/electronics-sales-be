package com.tip.b18.electronicsales.dto;

import com.tip.b18.electronicsales.constants.MessageConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountRegisterDTO extends AccountLoginDTO{
    @NotBlank(message = MessageConstant.ERROR_VALUE_REQUIRED)
    private String fullName;
}
