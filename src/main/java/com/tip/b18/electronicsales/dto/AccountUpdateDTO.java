package com.tip.b18.electronicsales.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class AccountUpdateDTO {
      private String fullName;
      private String email;
      private String phoneNumber;
      private LocalDate dateOfBirth;
      private String address;
      private Boolean gender;
      private String avatarUrl;
      private int totalQuantity;
}
