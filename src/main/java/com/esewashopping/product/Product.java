package com.esewashopping.product;

import com.esewashopping.category.Category;
import com.esewashopping.customer.Customer;
import com.esewashopping.shared.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    private String name;

    private Float price;

    private Integer quantity;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String productImage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Customer customer;

}

