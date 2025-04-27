package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.dto.ProductDTO;
import com.tip.b18.electronicsales.entities.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    CustomPage<ProductDTO> viewProducts(String search, int page, int limit, UUID categoryId, UUID brandId, String orderBy);
    ProductDTO viewProductDetails(UUID id);
    void addProduct(ProductDTO productDTO);
    void deleteProduct(UUID id);
    void updateProduct(UUID id, ProductDTO productDTO);
    void updateStockProducts(List<OrderDetailDTO> detailDTOList);
    List<Product> findProductsById(List<OrderDetailDTO> orderDetailDTOList);
    Product findProductById(UUID uuid);
    int getQuantityNewProducts(LocalDateTime startDay, LocalDateTime endDay);
}
