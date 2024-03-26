package com.esewashopping.cart;


import java.util.List;

public interface CartService {
    String addToCart(CartDto cartDto, Integer pId, Integer cId);


    String deleteCart(Integer cartId);

    List<CartResponseDto> getAllCart(Integer cID);

    String deleteAll(Integer cId);
}
