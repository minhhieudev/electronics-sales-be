package com.tip.b18.electronicsales.dto;

import com.tip.b18.electronicsales.dto.base.BaseAccountDTO;
import lombok.Data;

@Data
public class AccountDTO extends BaseAccountDTO {
    private String email;
    private String address;
}
