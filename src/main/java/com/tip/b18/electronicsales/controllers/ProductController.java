package com.tip.b18.electronicsales.controllers;

import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.ProductDTO;
import com.tip.b18.electronicsales.dto.ResponseDTO;
import com.tip.b18.electronicsales.services.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseDTO<CustomPage<ProductDTO>> viewProducts(@RequestParam(name = "search", defaultValue = "") String search,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "limit", defaultValue = "6") int limit,
                                                            @RequestParam(name = "categoryId", defaultValue = "") UUID categoryId,
                                                            @RequestParam(name = "brandId", defaultValue = "") UUID brandId,
                                                            @Parameter(description = "Chọn kiểu sắp xếp", schema = @Schema(allowableValues = {"newest", "bestseller", "priceAsc", "priceDesc", "priceDiscountAsc", "priceDiscountDesc"}))
                                                            @RequestParam(name = "orderBy", defaultValue = "") String orderBy){
        ResponseDTO<CustomPage<ProductDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus("success");
        responseDTO.setData(productService.viewProducts(search, page, limit, categoryId, brandId, orderBy));

        return responseDTO;
    }
}
