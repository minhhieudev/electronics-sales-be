package com.tip.b18.electronicsales.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountPersonalDTO extends AccountDTO{
    private LocalDate birthDay;
    private String numberPhone;
    private boolean gender;
}
