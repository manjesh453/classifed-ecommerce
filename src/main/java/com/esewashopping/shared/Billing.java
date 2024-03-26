package com.esewashopping.shared;

import com.esewashopping.customer.Customer;
import com.esewashopping.customer.CustomerRepo;
import com.esewashopping.exception.NotFoundException;
import com.esewashopping.exception.ResourceNotFoundException;
import com.esewashopping.order.OrderRepo;
import com.esewashopping.order.Orders;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class Billing {

    private final OrderRepo orderRepo;

    private final CustomerRepo customerRepo;

    public void print(Integer uId) throws IOException {
        String path = ErrorMessage.path;
        File file = new File(path);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception ex) {
            throw new NotFoundException("File not found");
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            Customer customer = customerRepo.findById(uId).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", uId));
            List<Orders> ordersList = orderRepo.findByCustomerAndDeliveryStatus(customer, Status.PENDING);
            writer.write("****************** Your Purchase Order on:  " + LocalDateTime.now() + " ***********************************");
            for (Orders orders : ordersList) {
                write(writer, orders);
            }
        }
    }

    private void write(BufferedWriter writer, Orders orders) throws IOException {
        writer.write(orders.getProductName() + "|");
        writer.write(orders.getQuantity() + "|");
        writer.write(orders.getDeliveryAddress() + "|");
        writer.newLine();
    }
}
