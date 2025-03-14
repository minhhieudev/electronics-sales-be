package com.tip.b18.electronicsales.dto;

import com.tip.b18.electronicsales.constants.MessageConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountLoginDTO {
    @NotBlank(message = MessageConstant.ERROR_VALUE_REQUIRED)
    private String userName;
    @NotBlank(message = MessageConstant.ERROR_VALUE_REQUIRED)
    private String password;
}
