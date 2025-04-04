package com.tip.b18.electronicsales.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tip.b18.electronicsales.enums.Delivery;
import com.tip.b18.electronicsales.enums.PaymentMethod;
import com.tip.b18.electronicsales.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private UUID id;
    private String orderCode;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Status status;
    private PaymentMethod paymentMethod;
    private Delivery delivery;
    private BigDecimal feeDelivery;
    private BigDecimal totalPrice;
    private List<OrderDetailDTO> items;
}
