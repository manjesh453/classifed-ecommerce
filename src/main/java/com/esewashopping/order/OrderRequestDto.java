package com.esewashopping.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class OrderRequestDto {
    @JsonAlias({"delivery_address","Delivery-Address"})
    private String deliveryAddress;
}
