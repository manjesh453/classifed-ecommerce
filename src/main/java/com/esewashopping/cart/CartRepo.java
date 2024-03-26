package com.esewashopping.cart;

import com.esewashopping.customer.Customer;
import com.esewashopping.shared.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Carts, Integer> {
    List<Carts> findByCustomerAndStatus(Customer customer, Status status);

    List<Carts>findAllByCustomer(Customer customer);

    List<Carts>findByStatus(Status status);

}
