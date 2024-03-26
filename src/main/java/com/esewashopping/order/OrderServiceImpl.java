package com.esewashopping.order;

import com.esewashopping.cart.CartRepo;
import com.esewashopping.cart.Carts;
import com.esewashopping.customer.Customer;
import com.esewashopping.customer.CustomerRepo;
import com.esewashopping.exception.ResourceNotFoundException;
import com.esewashopping.product.Product;
import com.esewashopping.product.ProductRepo;
import com.esewashopping.shared.Billing;
import com.esewashopping.shared.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    private final CustomerRepo customerRepo;

    private final ProductRepo productRepo;

    private final CartRepo cartRepo;

    private final ModelMapper modelMapper;

    @Override
    public String orderItem(OrderRequestDto requestDto, Integer cID) {
        Customer customer = customerRepo.findById(cID).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cID));
        List<Carts> carts = cartRepo.findByCustomerAndStatus(customer, Status.ACTIVE);
        List<Orders> ordersList = new ArrayList<>();
        String response = "";
        for (Carts cart : carts) {
            Orders orders = new Orders();
            orders.setCustomer(customer);
            orders.setProductName(cart.getProductName());
            orders.setDeliveryAddress(requestDto.getDeliveryAddress());
            orders.setTakeFrom(cart.getProductOwner());
            orders.setDeliveryStatus(Status.PENDING);
            orders.setQuantity(cart.getQuantity());
            orders.setPaymentStatus(Status.PENDING);
            if (addToOrder(cart)) {
                ordersList.add(orders);
            }
        }
        if (!ordersList.isEmpty()) {
            orderRepo.saveAll(ordersList);
            return response + "Make you Payment";
        } else {
            return response + "Order Out of stock";
        }
    }

    @Override
    public String changeStatus(Integer orderId) {
        Orders orders = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "OrderId", orderId));
        orders.setDeliveryStatus(Status.DELIVERED);
        return "Status has been delivered";
    }


    @Override
    public String afterPayment(Integer cID) throws IOException {
        Customer customer = customerRepo.findById(cID).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cID));
        List<Orders> ordersList = orderRepo.findByCustomerAndPaymentStatus(customer, Status.PENDING);
        for (Orders orders : ordersList) {
            orders.setPaymentStatus(Status.VERIFIED);
            orderRepo.save(orders);
            Billing billing = new Billing(orderRepo, customerRepo);
            billing.print(cID);
        }
        return "Thank you For your Orders";
    }

    @Override
    public List<OrderResponseDto> getOrderListById(Integer cID) {
        Customer customer = customerRepo.findById(cID).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cID));
        List<Orders> ordersList = orderRepo.findByCustomerAndPaymentStatus(customer, Status.VERIFIED);
        return ordersList.stream().map(li -> modelMapper.map(li, OrderResponseDto.class)).toList();
    }

    @Override
    public List<OrderResponseDto> getAllApproveOrder() {
        List<Orders> ordersList = orderRepo.findByPaymentStatus(Status.VERIFIED);
        return ordersList.stream().map(li -> modelMapper.map(li, OrderResponseDto.class)).toList();
    }

    @Override
    public List<OrderResponseDto> beforePayment(Integer cID) {
        Customer customer = customerRepo.findById(cID).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cID));
        List<Orders> ordersList = orderRepo.findByCustomerAndPaymentStatus(customer, Status.PENDING);
        return ordersList.stream().map(li -> modelMapper.map(li, OrderResponseDto.class)).toList();
    }

    @Override
    public String orderSingleCartItem(OrderRequestDto requestDto, Integer cartId, Integer cId) {
        Carts cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));
        Customer customer = customerRepo.findById(cId).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cId));
        if (cart.getStatus() == Status.ACTIVE) {
            Orders order = new Orders();
            order.setCustomer(customer);
            order.setTakeFrom(cart.getProductOwner());
            order.setPaymentStatus(Status.PENDING);
            order.setDeliveryStatus(Status.PENDING);
            order.setDeliveryAddress(requestDto.getDeliveryAddress());
            order.setQuantity(cart.getQuantity());
            order.setProductImage(cart.getProductImage());
            order.setTime(LocalDateTime.now());
            order.setProductName(cart.getProductName());
            Product product = productRepo.findByNameAndCustomerFullName(cart.getProductName(), cart.getProductOwner());
            if (product.getQuantity() >= order.getQuantity()) {
                product.setQuantity(product.getQuantity() - cart.getQuantity());
                productRepo.save(product);
                cartRepo.delete(cart);
                orderRepo.save(order);
                return "Make your Payment";
            } else {
                return "Product out of stock";
            }
        } else {
            return "This order has been already Purchase";
        }
    }

    private boolean addToOrder(Carts cart) {
        if (decreaseQuantity(cart)) {
            cart.setStatus(Status.DELETED);
            return true;
        } else {
            return false;
        }
    }

    private boolean decreaseQuantity(Carts cart) {
        Product product = productRepo.findByNameAndCustomerFullName(cart.getProductName(), cart.getProductOwner());
        if (cart.getQuantity() > product.getQuantity()) {
            return false;
        } else {
            product.setQuantity(product.getQuantity() - cart.getQuantity());
            productRepo.save(product);
            return true;
        }
    }

    @Scheduled(cron = "0 0 */1 * * *")
    private void checkValidity() {
        List<Orders> ordersList = orderRepo.findByPaymentStatus(Status.PENDING);
        for (Orders order : ordersList) {
            LocalDateTime time = order.getTime();
            if (time == LocalDateTime.now().plusHours(24)) {
                Product product = productRepo.findByNameAndCustomerFullName(order.getProductName(), order.getTakeFrom());
                product.setQuantity(product.getQuantity() + order.getQuantity());
                Carts cart = Carts.builder()
                        .customer(order.getCustomer())
                        .productName(order.getProductName())
                        .productImage(order.getProductImage())
                        .quantity(order.getQuantity())
                        .status(Status.ACTIVE)
                        .productOwner(order.getTakeFrom())
                        .productPrice(product.getPrice())
                        .build();
                cartRepo.save(cart);
                productRepo.save(product);
                orderRepo.delete(order);
            }
        }
    }
}
