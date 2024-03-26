package com.esewashopping.product;

import com.esewashopping.category.Category;
import com.esewashopping.customer.Customer;
import com.esewashopping.shared.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<Product>findByCategory(Category category);

    List<Product>findByNameContains(String name);

    List<Product>findByCustomer(Customer customer);

    @Query("select p from Product p where p.name = :name and p.customer = :customer")
    Product findByNameForCustomer(@Param("name") String name, @Param("customer") Customer customer);

    @Query("select p from Product p where p.name=:name and p.customer.fullName=:fullName")
    Product findByNameAndCustomerFullName(String name,String fullName);

    List<Product>findProductByStatus(Status status);


}
