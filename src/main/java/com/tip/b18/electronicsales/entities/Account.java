package com.tip.b18.electronicsales.entities;

import com.tip.b18.electronicsales.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Account extends BaseEntity {
    @Column(name = "number_phone", unique = true, columnDefinition = "CHAR(10)")
    private String numberPhone;

    @Column(name = "user_name", unique = true, columnDefinition = "CHAR(30)")
    private String userName;

    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(60)")
    private String password;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "full_name", nullable = false, columnDefinition = "VARCHAR(50)")
    private String fullName;

    private boolean role;

    @Column(name = "address", columnDefinition = "VARCHAR(100)")
    private String address;

    private boolean gender;

    @Column(name = "birth_day")
    private LocalDate birthDay;
}
