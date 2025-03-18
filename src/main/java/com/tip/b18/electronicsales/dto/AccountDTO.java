package com.tip.b18.electronicsales.dto;

import com.tip.b18.electronicsales.dto.base.BaseAccountsDTO;
import lombok.Data;

@Data
public class AccountDTO extends BaseAccountsDTO {
    private String email;
    private String address;
    private String avatarUrl;
}
