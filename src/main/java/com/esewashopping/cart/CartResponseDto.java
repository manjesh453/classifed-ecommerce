package com.esewashopping.cart;

import lombok.Data;

@Data
public class CartResponseDto {

    private int quantity;

    private String productName;

    private Float productPrice;

    private String productImage;
}
