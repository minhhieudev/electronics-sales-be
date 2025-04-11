package com.tip.b18.electronicsales.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDTO {
    private UUID id;
    private UUID cartId;
    private UUID productId;
    private String sku;
    private String name;
    private String color;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private int stock;
    private int quantity;
    private String mainImageUrl;
    private List<String> colors;
}
