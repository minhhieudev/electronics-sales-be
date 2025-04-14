package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.dto.*;
import com.tip.b18.electronicsales.entities.Account;
import com.tip.b18.electronicsales.entities.Order;
import com.tip.b18.electronicsales.enums.Delivery;
import com.tip.b18.electronicsales.enums.PaymentMethod;
import com.tip.b18.electronicsales.enums.Status;
import com.tip.b18.electronicsales.exceptions.NotFoundException;
import com.tip.b18.electronicsales.exceptions.IllegalStateException;
import com.tip.b18.electronicsales.mappers.OrderMapper;
import com.tip.b18.electronicsales.mappers.ProductMapper;
import com.tip.b18.electronicsales.mappers.TupleMapper;
import com.tip.b18.electronicsales.repositories.OrderRepository;
import com.tip.b18.electronicsales.services.*;
import com.tip.b18.electronicsales.utils.CompareUtil;
import com.tip.b18.electronicsales.utils.OrderUtil;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final TupleMapper tupleMapper;
    private final OrderDetailService orderDetailService;
    private final @Lazy AccountService accountService;
    private final ProductService productService;
    private final CartService cartService;

    @Override
    public CustomPage<OrderDTO> viewOrders(String search, int page, int limit, Status status, PaymentMethod paymentMethod, Delivery delivery) {
        Pageable pageable = SecurityUtil.isAdminRole() ? PageRequest.of(page, limit) : Pageable.unpaged();
        UUID accountId = SecurityUtil.isAdminRole() ? null : SecurityUtil.getAuthenticatedUserId();

        Page<Order> orderList = orderRepository.findAllByConditions(
                search, accountId, status, paymentMethod, delivery, pageable);

        PageInfoDTO pageInfoDTO = SecurityUtil.isAdminRole()
                ? new PageInfoDTO(orderList.getTotalElements(), orderList.getTotalPages())
                : null;

        List<UUID> uuidList = SecurityUtil.isAdminRole() ? null : orderList.stream().map(Order::getId).toList();

        return SecurityUtil.isAdminRole()
                ? new CustomPage<>(orderMapper.toOrderDTOListByAdmin(orderList), pageInfoDTO)
                : new CustomPage<>(orderMapper.toOrderDTOListByUser(orderList, orderDetailService.findAllByOrderId(uuidList)), null);
    }

    @Override
    public OrderDTO viewOrderDetails(UUID id) {
        Optional<Order> order = SecurityUtil.isAdminRole()
                ? orderRepository.findById(id)
                : orderRepository.findByIdAndAccountId(id, SecurityUtil.getAuthenticatedUserId());

        if(order.isEmpty()){
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_PRODUCT);
        }

        return orderMapper.toOrderDTO(order.get(), orderDetailService.findAllByOrderId(List.of(id)));
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Account account = accountService.findById(SecurityUtil.getAuthenticatedUserId());
        productService.updateStockProducts(orderDTO.getItems());
        Order order = orderRepository.save(orderMapper.toOrder(orderDTO, OrderUtil.generateOrderCode(), account));
        int totalQuantity = cartService.getTotalQuantityItemInCartByAccountId();
        List<OrderDetailDTO> orderDetailDTOList = orderDetailService.createOrderDetails(order, orderDTO.getItems());
        return orderMapper.createOrderResponse(order, orderDetailDTOList, totalQuantity);
    }

    @Override
    public void updateOrder(OrderDTO orderDTO) {
        boolean isAdmin = SecurityUtil.isAdminRole();
        Optional<Order> optionalOrder = isAdmin
                ? orderRepository.findById(orderDTO.getId())
                : orderRepository.findByIdAndAccountId(orderDTO.getId(), SecurityUtil.getAuthenticatedUserId());

        Order order = optionalOrder.orElseThrow(() -> new NotFoundException(MessageConstant.ERROR_NOT_FOUND_ORDER));

        if(order.getStatus().equals(Status.COMPLETED) || order.getStatus().equals(Status.CANCELED)){
            throw new IllegalStateException(String.format(MessageConstant.ERROR_UPDATE_ORDER, order.getStatus().getDisplayName()));
        }

        if(isAdmin){
            if(!CompareUtil.compare(orderDTO.getStatus(), order.getStatus())){
                order.setStatus(orderDTO.getStatus());
            }else {
                return;
            }
        }else{
            if(!order.getStatus().equals(Status.PENDING)){
                throw new IllegalStateException(String.format(MessageConstant.ERROR_UPDATE_ORDER, order.getStatus().getDisplayName()));
            }
            if(!CompareUtil.compare(orderDTO.getFullName(), order.getFullName())){
                order.setFullName(orderDTO.getFullName());
            }

            if(!CompareUtil.compare(orderDTO.getPhoneNumber(), order.getPhoneNumber())){
                order.setPhoneNumber(orderDTO.getPhoneNumber());
            }

            if(!CompareUtil.compare(orderDTO.getAddress(), order.getAddress())){
                order.setAddress(orderDTO.getAddress());
            }

            if(!CompareUtil.compare(orderDTO.getStatus(), order.getStatus())){
                order.setStatus(Status.CANCELED);
            }
        }
        orderRepository.save(order);
    }

    @Override
    public int getQuantityNewOrders(LocalDateTime startDay, LocalDateTime endDay) {
        return orderRepository.countQuantityNewOrders(startDay, endDay, Status.CANCELED);
    }

    @Override
    public CustomList<ProductDTO> getTopProducts(int limit, String startDayReq, String endDayReq) {
        Pageable pageable = Pageable.unpaged();
        if(limit > 0){
            pageable = PageRequest.of(0, limit);
        }

        LocalDateTime startDay = null;
        LocalDateTime endDay = LocalDate.now().atTime(LocalTime.MAX);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        if(startDayReq != null){
            startDay = LocalDate.parse(startDayReq, formatter).atStartOfDay();
        }

        if(endDayReq != null){
            endDay = LocalDate.parse(endDayReq, formatter).atTime(LocalTime.MAX);
        }

        List<Tuple> tuples = orderRepository.getTopProducts(pageable, startDay, endDay, Status.CANCELED);
        return new CustomList<>(tupleMapper.toProductDTOList(tuples));
    }
}
