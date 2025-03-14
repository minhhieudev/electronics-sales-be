package com.tip.b18.electronicsales.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomPage<T> {
    private List<T> items;
    private PageInfoDTO pageInfo;
}
