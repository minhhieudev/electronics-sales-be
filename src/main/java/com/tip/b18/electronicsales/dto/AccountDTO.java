package com.tip.b18.electronicsales.dto;

import com.tip.b18.electronicsales.dto.base.BaseAccountsDTO;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AccountDTO extends BaseAccountsDTO {
    private String email;
    private String address;
    private String avatarUrl;
    private LocalDate dateOfBirth;
}
