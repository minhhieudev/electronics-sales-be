package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.ProductDTO;
import java.util.UUID;

public interface ProductService {
    CustomPage<ProductDTO> viewProducts(String search, int page, int limit, UUID categoryId, UUID brandId, String orderBy);
}
