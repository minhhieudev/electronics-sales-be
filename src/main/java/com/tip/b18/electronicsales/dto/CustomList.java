package com.tip.b18.electronicsales.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomList <T>{
    List<T> items;
}
