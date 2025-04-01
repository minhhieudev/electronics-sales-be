package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.entities.Product;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    void addImages(List<String> images, Product product);
    void deleteImages(UUID id);
}
