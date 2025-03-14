package com.tip.b18.electronicsales.entities;

import com.tip.b18.electronicsales.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Product extends BaseEntity {
    @Column(name = "sku", nullable = false, columnDefinition = "CHAR(25)")
    private String sku;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    private int stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private int rom;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "discount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discount;

    @Column(name = "discount_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountPrice;

    private int warranty;

    @Column(name = "main_image", nullable = false, columnDefinition = "TEXT")
    private String mainImage;
}
