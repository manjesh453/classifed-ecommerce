package com.esewashopping.order;

import com.esewashopping.customer.Customer;
import com.esewashopping.shared.Status;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepo extends JpaRepository<Orders, Integer> {
    List<Orders> findByCustomerAndPaymentStatus(Customer customer,Status status);

    List<Orders>findByPaymentStatus(Status status);

    List<Orders>findByCustomerAndDeliveryStatus(Customer customer,Status status);

}
