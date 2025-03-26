package com.tip.b18.electronicsales.mappers;

import com.tip.b18.electronicsales.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    List<ProductDTO> toProductDTOList(Page<ProductDTO> productDTOS);
}
