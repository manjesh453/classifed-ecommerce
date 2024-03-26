package com.esewashopping.order;

import com.esewashopping.customer.Customer;
import com.esewashopping.shared.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private Status deliveryStatus;

    private String takeFrom;

    private String productName;

    private Integer quantity;

    private String productImage;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private Status paymentStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer customer;
}
