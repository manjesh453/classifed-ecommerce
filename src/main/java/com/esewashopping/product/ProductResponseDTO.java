package com.esewashopping.product;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private int pid;
    private String name;
    private Float price;
    private Integer quantity;
    private String description;
    private String productImage;
}
