package com.tip.b18.electronicsales.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountUpdateDTO {
      private String fullName;
      private String email;
      private String numberPhone;
      private LocalDate birthDay;
      private String address;
      private boolean gender;
      private String avatarUrl;
}
