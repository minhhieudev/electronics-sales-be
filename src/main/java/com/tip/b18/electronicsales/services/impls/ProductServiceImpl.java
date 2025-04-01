package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.PageInfoDTO;
import com.tip.b18.electronicsales.dto.ProductDTO;
import com.tip.b18.electronicsales.entities.*;
import com.tip.b18.electronicsales.exceptions.AlreadyExistsException;
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
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
        if(productRepository.existsBySku(productDTO.getSku())){
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
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageConstant.ERROR_NOT_FOUND_PRODUCT)
        );

        imageService.deleteImages(product.getId());

        List<Color> colorList = productColorService.getProductColors(product.getId());
        colorService.deleteColors(colorList);

        productRepository.delete(product);
    }
}