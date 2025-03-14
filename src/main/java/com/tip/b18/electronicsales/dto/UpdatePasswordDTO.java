package com.tip.b18.electronicsales.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String newPassword;
    private String oldPassword;
}
