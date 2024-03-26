package com.esewashopping.customer;

import com.esewashopping.shared.Status;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CustomerService {

    String updateCustomer(CustomerDto customerDto, Integer cid);

    CustomerDto findCustomerById(Integer cid);

    void deleteCustomer(Integer cid);

    List<CustomerDto> findByStatus(Status status);

    String changeStatus(Integer cid);

    List<CustomerDto> getAllCustomer();


    void sendVerification(Customer customer, String siteURL) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String code);

}
