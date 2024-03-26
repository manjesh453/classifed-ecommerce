package com.esewashopping.category;

import java.util.List;

public interface CategoryService {

    String createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto,Integer catId);

    void deleteCategory(Integer catId);

    CategoryDto categoryById(Integer catId);

    List<CategoryResponseDTO> getAllCategory();
}
