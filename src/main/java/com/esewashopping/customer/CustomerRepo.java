package com.esewashopping.customer;

import com.esewashopping.shared.Role;
import com.esewashopping.shared.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Integer> {
    List<Customer> findByStatus(Status status);
    Customer findByVerificationCode(String code);
    Customer findByRole(Role role);

    Customer findByEmail(String email);

    @Query("select c from Customer as c where c.email=:username")
    Optional<Customer> findByEmails(String username);


}
