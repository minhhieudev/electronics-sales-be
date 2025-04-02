package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import java.util.List;
import java.util.UUID;

public interface OrderDetailService {
    List<OrderDetailDTO> findAllByOrderId(List<UUID> uuidList);
}
