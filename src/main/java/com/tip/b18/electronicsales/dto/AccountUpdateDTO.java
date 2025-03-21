package com.tip.b18.electronicsales.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AccountUpdateDTO {
      private String fullName;
      private String email;
      private String phoneNumber;
      private LocalDate dateOfBirth;
      private String address;
      private boolean gender;
      private String avatarUrl;
}
