package com.esewashopping.category;

import com.esewashopping.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final ModelMapper modelMapper;

    private final CategoryRepo categoryRepo;
    @Override
    public String createCategory(CategoryDto categoryDto) {
        Category existCategory=categoryRepo.findByName(categoryDto.getName());
        if(existCategory==null) {
            Category category = modelMapper.map(categoryDto, Category.class);
            categoryRepo.save(category);
            return "New category has been added";
        }else {
            return "Sorry this Category name already exist";
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer catId) {
        Category category=categoryRepo.findById(catId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",catId));
        if(categoryDto.getName()!=null){
            category.setName(categoryDto.getName());
        }
        Category updateCategory=categoryRepo.save(category);
        return modelMapper.map(updateCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer catId) {
        Category category=categoryRepo.findById(catId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",catId));
        categoryRepo.delete(category);
    }

    @Override
    public CategoryDto categoryById(Integer catId) {
        Category category=categoryRepo.findById(catId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",catId));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {
        List<Category> listOfCategory = categoryRepo.findAll();
        return listOfCategory.stream()
                .map(category -> modelMapper.map(category, CategoryResponseDTO.class)).toList();
    }

}
