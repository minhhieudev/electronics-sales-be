package com.tip.b18.electronicsales.mappers;

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
}
