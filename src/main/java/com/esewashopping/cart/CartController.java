package com.esewashopping.cart;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/add-to-cart/{pId}/{cId}")
    public String addToCart(@RequestBody CartDto cartDto, @PathVariable Integer pId, @PathVariable Integer cId) {
        return cartService.addToCart(cartDto, pId, cId);
    }

    @PostMapping("/delete-by-id/{cartId}")
    public String deleteCartById(@PathVariable Integer cartId) {
        return cartService.deleteCart(cartId);
    }

    @GetMapping("/get-cart-of-customer/{cId}")
    public List<CartResponseDto> getAllCartById(@PathVariable Integer cId) {
        return cartService.getAllCart(cId);
    }

    @PostMapping("/delete-all-cart/{cId}")
    public String deleteAllCartByCustomer(@PathVariable Integer cId) {
        return cartService.deleteAll(cId);
    }

}
