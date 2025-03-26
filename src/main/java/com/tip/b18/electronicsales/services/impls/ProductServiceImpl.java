package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.PageInfoDTO;
import com.tip.b18.electronicsales.dto.ProductDTO;
import com.tip.b18.electronicsales.mappers.ProductMapper;
import com.tip.b18.electronicsales.repositories.ProductCriteria;
import com.tip.b18.electronicsales.repositories.ProductRepository;
import com.tip.b18.electronicsales.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCriteria productCriteria;

    @Override
    public CustomPage<ProductDTO> viewProducts(String search, int page, int limit, UUID categoryId, UUID brandId, String orderBy) {
        Page<ProductDTO> products = productCriteria.searchProductsByConditions(search, page, limit, categoryId, brandId, orderBy);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalPages(products.getTotalPages());
        pageInfoDTO.setTotalElements(products.getTotalElements());

        CustomPage<ProductDTO> productDTOs = new CustomPage<>();
        productDTOs.setPageInfo(pageInfoDTO);
        productDTOs.setItems(productMapper.toProductDTOList(products));

        return productDTOs;
    }
}
