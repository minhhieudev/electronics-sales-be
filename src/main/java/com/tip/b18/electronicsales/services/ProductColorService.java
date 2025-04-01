package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.entities.Color;
import com.tip.b18.electronicsales.entities.Product;
import com.tip.b18.electronicsales.entities.ProductColor;
import jakarta.persistence.Tuple;

import java.util.List;
import java.util.UUID;

public interface ProductColorService {
    void addProductColors(List<Color> colorList, Product product);
    List<Color> getProductColors(UUID id);
    void deleteProductColors(List<ProductColor> productColorList);
}
