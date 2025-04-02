package com.tip.b18.electronicsales.mappers;

import com.tip.b18.electronicsales.dto.OrderDTO;
import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.entities.Order;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default List<OrderDTO> toOrderDTOListByAdmin(Page<Order> orders){
        return orders
                .getContent()
                .stream()
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .orderCode(order.getOrderCode())
                        .phoneNumber(order.getAccount().getPhoneNumber())
                        .totalPrice(order.getTotalPrice())
                        .status(order.getStatus())
                        .paymentMethod(order.getPaymentMethod())
                        .delivery(order.getDelivery())
                        .build())
                .collect(Collectors.toList());
    }

    default List<OrderDTO> toOrderDTOListByUser(Page<Order> orders, List<OrderDetailDTO> detailDTOList){
        return orders
                .getContent()
                .stream()
                .map(order ->
                {
                    List<OrderDetailDTO> orderDetails = detailDTOList.stream()
                            .filter(orderDetail -> order.getId().equals(orderDetail.getOrderId()))
                            .peek(orderDetail -> orderDetail.setOrderId(null))
                            .toList();
                    return OrderDTO.builder()
                            .id(order.getId())
                            .orderCode(order.getOrderCode())
                            .totalPrice(order.getTotalPrice())
                            .status(order.getStatus())
                            .paymentMethod(order.getPaymentMethod())
                            .delivery(order.getDelivery())
                            .items(orderDetails)
                            .build();
                }).collect(Collectors.toList());
    }

    default OrderDTO toOrderDTO(Order order, List<OrderDetailDTO> detailDTOList){
        List<OrderDetailDTO> orderDetails = detailDTOList.stream()
                .peek(orderDetail -> orderDetail.setOrderId(null))
                .toList();

        OrderDTO.OrderDTOBuilder builder = OrderDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .delivery(order.getDelivery())
                .totalPrice(order.getTotalPrice())
                .items(orderDetails);

        if(!SecurityUtil.isAdminRole()){
            builder.fullName(order.getFullName());
        }
        return builder.build();
    }
}
