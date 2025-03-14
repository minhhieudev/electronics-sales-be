package com.tip.b18.electronicsales.entities;


import com.tip.b18.electronicsales.entities.Account;
import com.tip.b18.electronicsales.entities.base.BaseIdEntity;
import com.tip.b18.electronicsales.enums.Delivery;
import com.tip.b18.electronicsales.enums.PaymentMethod;
import com.tip.b18.electronicsales.enums.Status;
import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "`order`")
@Data
public class Order extends BaseIdEntity {
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "fee_delivery", precision = 10, scale = 2, nullable = false)
    private BigDecimal feeDelivery;

    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(100)")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery", nullable = false)
    private Delivery delivery;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}
