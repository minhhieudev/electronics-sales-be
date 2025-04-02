package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.entities.OrderDetail;
import com.tip.b18.electronicsales.mappers.OrderDetailMapper;
import com.tip.b18.electronicsales.repositories.OrderDetailRepository;
import com.tip.b18.electronicsales.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetailDTO> findAllByOrderId(List<UUID> uuidList) {
        return orderDetailMapper.toOrderDetailDTOs(orderDetailRepository.findAllByOrderIdIn(uuidList));
    }
}
