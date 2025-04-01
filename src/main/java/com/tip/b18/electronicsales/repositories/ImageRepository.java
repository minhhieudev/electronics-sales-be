package com.tip.b18.electronicsales.repositories;

import com.tip.b18.electronicsales.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    void deleteAllByProductId(UUID id);
}
