package com.esewashopping.cart;

import com.esewashopping.customer.Customer;
import com.esewashopping.shared.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    private Integer quantity;

    private String productName;

    private Float productPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String productOwner;

    private String productImage;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer customer;
}
