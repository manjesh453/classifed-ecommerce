package com.esewashopping.order;

import com.esewashopping.shared.Status;
import lombok.Data;

@Data
public class OrderResponseDto {

    private int orderId;

    private String deliveryAddress;

    private Status deliveryStatus;

    private String takeFrom;

    private String productName;
}
