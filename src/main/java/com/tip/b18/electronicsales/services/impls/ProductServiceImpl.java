package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.OrderDetailDTO;
import com.tip.b18.electronicsales.dto.PageInfoDTO;
import com.tip.b18.electronicsales.dto.ProductDTO;
import com.tip.b18.electronicsales.entities.*;
import com.tip.b18.electronicsales.exceptions.AlreadyExistsException;
import com.tip.b18.electronicsales.exceptions.InsufficientStockException;
import com.tip.b18.electronicsales.exceptions.NotFoundException;
import com.tip.b18.electronicsales.mappers.ProductMapper;
import com.tip.b18.electronicsales.mappers.TupleMapper;
import com.tip.b18.electronicsales.repositories.*;
import com.tip.b18.electronicsales.services.*;
import com.tip.b18.electronicsales.utils.BigDecimalUtil;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCriteria productCriteria;
    private final TupleMapper tupleMapper;
    private final ColorService colorService;
    private final ProductColorService productColorService;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @Override
    public CustomPage<ProductDTO> viewProducts(String search, int page, int limit, UUID categoryId, UUID brandId, String orderBy) {
        Page<ProductDTO> products = productCriteria.searchProductsByConditions(search, page, limit, categoryId, brandId, orderBy);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalPages(products.getTotalPages());
        pageInfoDTO.setTotalElements(products.getTotalElements());

        CustomPage<ProductDTO> productDTOs = new CustomPage<>();
        productDTOs.setPageInfo(pageInfoDTO);
        productDTOs.setItems(productMapper.toProductDTOList(products));

        return productDTOs;
    }

    @Override
    public ProductDTO viewProductDetails(UUID id) {
        List<Tuple> tuples = productRepository.findProductById(id);
        if (tuples.isEmpty()) {
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_PRODUCT);
        }

        Tuple tuple = tuples.get(0);
        Product product = tuple.get("product", Product.class);

        List<String> images = tupleMapper.mapToList(tuples, "image");
        List<String> colors = tupleMapper.mapToList(tuples, "color");

        ProductDTO.ProductDTOBuilder builder = ProductDTO.builder();
        builder.id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .stock(product.getStock())
                .category(product.getCategory().getName())
                .brand(product.getBrand().getName())
                .price(product.getPrice())
                .warranty(product.getWarranty())
                .discount(product.getDiscount())
                .discountPrice(product.getDiscountPrice())
                .description(product.getDescription())
                .mainImageUrl(product.getMainImageUrl())
                .colors(colors)
                .images(images);
        if(!SecurityUtil.isAdminRole()){
            Integer quantitySold = tuple.get("quantitySold", Integer.class);
            builder.quantitySold(quantitySold != null ? quantitySold : 0);
        }
        return builder.build();
    }

    @Override
    @Transactional
    public void addProduct(ProductDTO productDTO) {
        if(productRepository.existsBySkuAndIsDeleted(productDTO.getSku(), false)){
            throw new AlreadyExistsException(MessageConstant.ERROR_PRODUCT_EXISTS);
        }

        Category category = categoryService.getCategoryById(UUID.fromString(productDTO.getCategory()));
        Brand brand = brandService.getBrandById(UUID.fromString(productDTO.getBrand()));

        BigDecimal discountPrice = BigDecimalUtil.calculateDiscountPrice(productDTO.getPrice(), productDTO.getDiscount());
        if(discountPrice != null){
            productDTO.setDiscountPrice(discountPrice);
        }else{
            productDTO.setDiscountPrice(productDTO.getPrice());
        }

        Product product = productRepository.save(productMapper.toProduct(productDTO, category, brand));
        List<Color> colorList = colorService.addNewColor(productDTO.getColors());

        productColorService.addProductColors(colorList, product);
        imageService.addImages(productDTO.getImages(), product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findByIdAndIsDeleted(id, false);
        if(product == null){
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_PRODUCT);
        }
        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(UUID id, ProductDTO productDTO) {
        Product product = productRepository.findByIdAndIsDeleted(id, false);
        if(product == null){
            throw new NotFoundException(MessageConstant.ERROR_NOT_FOUND_PRODUCT);
        }

        boolean isChange = false;

        if(productDTO.getSku() != null && !productDTO.getSku().equals(product.getSku())){
            if(productRepository.existsBySkuAndIsDeleted(productDTO.getSku(), false)){
                throw new AlreadyExistsException(MessageConstant.ERROR_PRODUCT_EXISTS);
            }

            product.setSku(productDTO.getSku());
            isChange = true;
        }

        if(productDTO.getName() != null && !productDTO.getName().equals(product.getName())){
            product.setName(productDTO.getName());
            isChange = true;
        }

        if(productDTO.getStock() != null && !productDTO.getStock().equals(product.getStock())){
            product.setStock(productDTO.getStock());
            isChange = true;
        }

        if(productDTO.getPrice() != null && !productDTO.getPrice().equals(product.getPrice())){
            product.setPrice(productDTO.getPrice());
            isChange = true;
        }

        if(productDTO.getDiscount() != null && !productDTO.getDiscount().equals(product.getDiscount())){
            BigDecimal discountPrice = BigDecimalUtil.calculateDiscountPrice(product.getPrice(), productDTO.getDiscount());
            if(discountPrice != null && !discountPrice.equals(product.getDiscountPrice())){
                product.setDiscountPrice(discountPrice);
            }
            product.setDiscount(productDTO.getDiscount());
            isChange = true;
        }

        if(productDTO.getDescription() != null && !productDTO.getDescription().equals(product.getDescription())){
            product.setDescription(productDTO.getDescription());
            isChange = true;
        }

        if(productDTO.getWarranty() != null && !productDTO.getWarranty().equals(product.getWarranty())){
            product.setWarranty(productDTO.getWarranty());
            isChange = true;
        }

        if(productDTO.getMainImageUrl() != null && !productDTO.getMainImageUrl().equals(product.getMainImageUrl())){
            product.setMainImageUrl(productDTO.getMainImageUrl());
            isChange = true;
        }

        if(productDTO.getCategory() != null){
            Category category = categoryService.getCategoryById(UUID.fromString(productDTO.getCategory()));
            if(productDTO.getCategory() != null && !category.equals(product.getCategory())){
                product.setCategory(category);
                isChange = true;
            }
        }

        if(productDTO.getBrand() != null){
            Brand brand = brandService.getBrandById(UUID.fromString(productDTO.getBrand()));
            if(brand != null && !brand.equals(product.getBrand())){
                product.setBrand(brand);
                isChange = true;
            }
        }

        if(productDTO.getImages() != null && !productDTO.getImages().isEmpty()){
            imageService.updateImagesByProductId(product, productDTO.getImages());
        }

        if(productDTO.getColors() != null && !productDTO.getColors().isEmpty()){
            productColorService.updateProductColorsByProductId(productDTO.getColors(), product);
        }

        if(isChange){
            productRepository.save(product);
        }
    }

    @Override
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 7)
    @Transactional
    public void scheduledProductCleanup() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Product> products = productRepository.findByIsDeletedTrueAndDeletedAtBefore(sevenDaysAgo);

        List<UUID> productIdList = new ArrayList<>();
        for (Product product : products){
            productIdList.add(product.getId());
        }

        imageService.deleteImages(productIdList);
        List<Color> colorList = productColorService.getColorsByProductColors(productIdList);
        colorService.deleteColors(colorList);
        productRepository.deleteAll(products);
    }

    @Override
    public void updateStockProducts(List<OrderDetailDTO> orderDetailDTOList) {
        List<UUID> uuidList = orderDetailDTOList
                .stream()
                .map(OrderDetailDTO::getId)
                .toList();

        List<Product> products = productRepository.findAllByIdInAndIsDeleted(uuidList, false);

        Map<UUID, Integer> map = orderDetailDTOList
                .stream()
                .collect(Collectors.toMap(OrderDetailDTO::getId, OrderDetailDTO::getQuantity));

        List<Product> productListToUpdate = products
                .stream()
                .filter(product -> map.containsKey(product.getId()))
                .peek(product -> {
                    int quantity = map.get(product.getId());
                    if(product.getStock() < quantity){
                        throw new InsufficientStockException(String.format(MessageConstant.ERROR_INSUFFICIENT_STOCK, product.getName()));
                    }
                    product.setStock(product.getStock() - quantity);
                })
                .toList();
        productRepository.saveAll(productListToUpdate);
    }

    @Override
    public List<Product> findProductsById(List<OrderDetailDTO> orderDetailDTOList) {
        List<UUID> uuidList = orderDetailDTOList
                .stream()
                .map(OrderDetailDTO::getId)
                .toList();

        return productRepository.findAllByIdInAndIsDeleted(uuidList, false);
    }
}