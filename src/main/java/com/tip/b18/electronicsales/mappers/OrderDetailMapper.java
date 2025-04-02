package com.tip.b18.electronicsales.mappers;

import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.entities.OrderDetail;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    default List<OrderDetailDTO> toOrderDetailDTOs(List<OrderDetail> orderDetails){
        return orderDetails
                .stream()
                .map(orderDetail -> {
                    OrderDetailDTO.OrderDetailDTOBuilder builder = OrderDetailDTO
                            .builder()
                            .sku(orderDetail.getProduct().getSku())
                            .name(orderDetail.getProduct().getName())
                            .quantity(orderDetail.getQuantity())
                            .priceAtTime(orderDetail.getPriceAtTime())
                            .totalPrice(orderDetail.getTotalPrice())
                            .color(orderDetail.getColor());

                    if(!SecurityUtil.isAdminRole()){
                        builder
                                .id(orderDetail.getId())
                                .orderId(orderDetail.getOrder().getId())
                                .mainImageUrl(orderDetail.getProduct().getMainImageUrl());
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }
}
