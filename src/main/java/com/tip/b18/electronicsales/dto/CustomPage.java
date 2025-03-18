package com.tip.b18.electronicsales.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
public class CustomPage<T> {
    private List<T> items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageInfoDTO pageInfo;
}
