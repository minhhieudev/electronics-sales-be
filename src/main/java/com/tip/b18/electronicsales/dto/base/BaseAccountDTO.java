package com.tip.b18.electronicsales.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class BaseAccountDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
    private String fullName;
    private String userName;
    private boolean role;
}
