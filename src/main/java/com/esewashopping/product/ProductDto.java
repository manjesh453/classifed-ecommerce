package com.esewashopping.product;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ProductDto {

    private String name;

    private Float price;

    private Integer quantity;

    private String description;

    @JsonAlias({"product_image","ProductImage"})
    private String productImage;
}
