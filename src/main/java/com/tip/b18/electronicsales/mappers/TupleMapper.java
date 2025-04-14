package com.tip.b18.electronicsales.mappers;

import com.tip.b18.electronicsales.dto.ProductDTO;
import jakarta.persistence.Tuple;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface TupleMapper {
    default List<String> mapToList(List<Tuple> result, String alias){
        return result.stream()
                .map(tuple -> tuple.get(alias, String.class))
                .distinct()
                .filter(Objects::nonNull)
                .toList();
    };

    default List<ProductDTO> toProductDTOList(List<Tuple> tuples){
        return tuples
                .stream()
                .map(tuple -> ProductDTO
                        .builder()
                        .name(tuple.get("productName", String.class))
                        .quantitySold(tuple.get("quantitySold", Long.class).intValue())
                        .build())
                .toList();
    }
}
