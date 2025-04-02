package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.OrderDTO;
import com.tip.b18.electronicsales.enums.Delivery;
import com.tip.b18.electronicsales.enums.PaymentMethod;
import com.tip.b18.electronicsales.enums.Status;

import java.util.UUID;

public interface OrderService {
    CustomPage<OrderDTO> viewOrders(String search, int page, int limit, Status status, PaymentMethod paymentMethod, Delivery delivery);
    OrderDTO viewOrderDetails(UUID id);
}
