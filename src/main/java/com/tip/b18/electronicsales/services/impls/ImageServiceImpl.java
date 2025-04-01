package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.entities.Image;
import com.tip.b18.electronicsales.entities.Product;
import com.tip.b18.electronicsales.repositories.ImageRepository;
import com.tip.b18.electronicsales.services.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public void addImages(List<String> images, Product product) {
        List<Image> imageList = new ArrayList<>();
        for(String imageUrl : images){
            Image image = new Image();
            image.setProduct(product);
            image.setUrl(imageUrl.trim());

            imageList.add(image);
        }
        imageRepository.saveAll(imageList);
    }

    @Override
    public void deleteImages(UUID id) {
        imageRepository.deleteAllByProductId(id);
    }
}
