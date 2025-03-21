package com.tip.b18.electronicsales.dto;

import lombok.Data;

@Data
public class AccountPersonalDTO extends AccountDTO{
    private String phoneNumber;
    private boolean gender;
}
