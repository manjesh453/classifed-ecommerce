package com.esewashopping.category;

import com.esewashopping.shared.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add-category")
    public String createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PostMapping("/update-category/{catId}")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Integer catId) {
        return categoryService.updateCategory(categoryDto, catId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{catId}")
    public ApiResponse deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
        return new ApiResponse("Category deleted successfully", true);
    }

    @GetMapping("/get-by-id/{catId}")
    public CategoryDto categoryById(@PathVariable Integer catId) {
        return categoryService.categoryById(catId);
    }

    @GetMapping("/get-all-category")
    public List<CategoryResponseDTO> getAllCategory() {
        return categoryService.getAllCategory();
    }

}
