package com.tip.b18.electronicsales.controllers;

import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.OrderDTO;
import com.tip.b18.electronicsales.dto.ResponseDTO;
import com.tip.b18.electronicsales.enums.Delivery;
import com.tip.b18.electronicsales.enums.PaymentMethod;
import com.tip.b18.electronicsales.enums.Status;
import com.tip.b18.electronicsales.services.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseDTO<CustomPage<OrderDTO>> viewOrders(@RequestParam(name = "search", defaultValue = "") String search,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "limit", defaultValue = "6") int limit,
                                                        @Parameter(description = "Chọn trạng thái", schema = @Schema(implementation = Status.class))
                                                        @RequestParam(name = "status", defaultValue = "") Status status,
                                                        @Parameter(description = "Chọn loại giao dịch", schema = @Schema(implementation = PaymentMethod.class))
                                                        @RequestParam(name = "transaction", defaultValue = "") PaymentMethod transaction,
                                                        @Parameter(description = "Chọn loại vận chuyển", schema = @Schema(implementation = Delivery.class))
                                                        @RequestParam(name = "delivery", defaultValue = "") Delivery delivery){
        ResponseDTO<CustomPage<OrderDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setData(orderService.viewOrders(search, page, limit, status, transaction, delivery));

        return responseDTO;
    }

    @GetMapping("/details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseDTO<OrderDTO> viewOrderDetails(@RequestParam("id") UUID id){
        ResponseDTO<OrderDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setData(orderService.viewOrderDetails(id));

        return responseDTO;
    }
}
