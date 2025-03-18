





package com.tip.b18.electronicsales.services.impls;

import com.tip.b18.electronicsales.constants.MessageConstant;
import com.tip.b18.electronicsales.dto.CategoriesDTO;
import com.tip.b18.electronicsales.dto.CategoryDTO;
import com.tip.b18.electronicsales.dto.CustomPage;
import com.tip.b18.electronicsales.dto.PageInfoDTO;
import com.tip.b18.electronicsales.entities.Category;
import com.tip.b18.electronicsales.exceptions.AlreadyExistsException;
import com.tip.b18.electronicsales.mappers.CategoryMapper;
import com.tip.b18.electronicsales.repositories.CategoryRepository;
import com.tip.b18.electronicsales.services.CategoryService;
import com.tip.b18.electronicsales.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CustomPage<CategoriesDTO> viewCategories(String search, int page, int limit) {
        CustomPage<CategoriesDTO> categoriesDTO = new CustomPage<>();;
        Pageable pageable;
        Page<Category> categories;

        if(SecurityUtil.isAdminRole()){
            pageable = PageRequest.of(page, limit);
            categories = categoryRepository.findByCategoryName(search, pageable);

            PageInfoDTO pageInfoDTO = new PageInfoDTO();
            pageInfoDTO.setTotalPages(categories.getTotalPages());
            pageInfoDTO.setTotalElements(categories.getTotalElements());

            categoriesDTO.setPageInfo(pageInfoDTO);
        }else{
            pageable = Pageable.unpaged();
            categories = categoryRepository.findAll(pageable);

            categories.forEach(category -> {category.setDescription(null);});
        }
        categoriesDTO.setItems(categoryMapper.toCategoriesDTO(categories));

        return categoriesDTO;
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.existsByName(categoryDTO.getName())){
            throw new AlreadyExistsException(MessageConstant.ERROR_CATEGORY_NAME_EXISTS);
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        categoryRepository.save(category);
    }
}








