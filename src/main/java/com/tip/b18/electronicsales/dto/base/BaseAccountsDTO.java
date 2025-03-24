package com.tip.b18.electronicsales.dto.base;

import lombok.Data;

@Data
public abstract class BaseAccountsDTO extends BaseAccountDTO{
    private Boolean gender;
    private String phoneNumber;
}
