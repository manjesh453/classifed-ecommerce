package com.esewashopping.category;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private int catId;
    private String name;

    public CategoryResponseDTO(int catId, String name) {
        this.catId = catId;
        this.name = name;
    }
}
