package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.entities.Color;
import com.tip.b18.electronicsales.entities.Product;
import com.tip.b18.electronicsales.entities.ProductColor;
import com.tip.b18.electronicsales.repositories.ProductColorRepository;
import com.tip.b18.electronicsales.services.ProductColorService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductColorServiceImpl implements ProductColorService {
    private final ProductColorRepository productColorRepository;

    @Override
    public void addProductColors(List<Color> colorList, Product product) {
        List<ProductColor> productColorList = new ArrayList<>();
        for (Color color : colorList) {
            ProductColor productColor = new ProductColor();
            productColor.setProduct(product);
            productColor.setColor(color);

            productColorList.add(productColor);
        }
        productColorRepository.saveAll(productColorList);
    }

    @Override
    public List<Color> getProductColors(UUID id) {
        List<Tuple> tupleList = productColorRepository.findAllByProductId(id);
        List<Color> colorList = new ArrayList<>();
        List<ProductColor> productColorList = new ArrayList<>();
        for (Tuple tuple : tupleList){
            ProductColor productColor = tuple.get("productColor", ProductColor.class);
            productColorList.add(productColor);

            if((Long)tuple.get(1) < 2){
                Color color = productColor.getColor();
                colorList.add(color);
            }
        }
        deleteProductColors(productColorList);
        return colorList;
    }

    @Override
    public void deleteProductColors(List<ProductColor> productColorList) {
        productColorRepository.deleteAll(productColorList);
    }
}
