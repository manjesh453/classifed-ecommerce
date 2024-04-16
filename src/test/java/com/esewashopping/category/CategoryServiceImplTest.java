package com.esewashopping.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepo categoryRepo;

    @Test
    void createCategory() {
        CategoryDto categoryDto=new CategoryDto("Food");
        when(categoryRepo.findByName(categoryDto.getName())).thenReturn(null);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(new Category());
        String expected=categoryService.createCategory(categoryDto);
        String actual="New category has been added";
        assertEquals(expected,actual);

    }

    @Test
    void updateCategory() {
        int id=1;
        CategoryDto categoryDto=new CategoryDto("New Category");
        Category category=new Category(1,"Old category");
        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepo.save(category)).thenReturn(category);
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        CategoryDto updateCategory= categoryService.updateCategory(categoryDto,id);
        assertEquals(categoryDto,updateCategory);
    }

    @Test
    void deleteCategory() {
        int id=1;
        Category category=new Category(1,"Old category");
        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepo).delete(category);
        categoryService.deleteCategory(id);
        verify(categoryRepo).delete(category);
    }

    @Test
    void categoryById() {
        int id=1;
        Category category=new Category(1,"New category");
        CategoryDto categoryDto=new CategoryDto("New Category");
        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        CategoryDto data=categoryService.categoryById(id);
        assertEquals(categoryDto,data);
    }

    @Test
    void getAllCategory() {
        List<CategoryResponseDTO> categoryDtoList= Arrays.asList( new CategoryResponseDTO(1,"Food"));
        List<Category>categoryList=Arrays.asList(new Category(1,"Food"));
        when(categoryRepo.findAll()).thenReturn(categoryList);
        when(modelMapper.map(categoryList.get(0), CategoryResponseDTO.class)).thenReturn(categoryDtoList.get(0));
        List<CategoryResponseDTO>list=categoryService.getAllCategory();
        assertEquals(categoryDtoList,list);
    }
}