package com.esewashopping.cart;

import com.esewashopping.customer.Customer;
import com.esewashopping.customer.CustomerRepo;
import com.esewashopping.exception.ResourceNotFoundException;
import com.esewashopping.product.Product;
import com.esewashopping.product.ProductRepo;
import com.esewashopping.shared.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final ModelMapper modelMapper;

    private final CustomerRepo customerRepo;

    private final ProductRepo productRepo;

    private final CartRepo cartRepo;

    @Override
    public String addToCart(CartDto cartDto, Integer pId, Integer cId) {
        Carts cart = modelMapper.map(cartDto, Carts.class);
        Product product = productRepo.findById(pId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", pId));
        Customer customer = customerRepo.findById(cId).orElseThrow(() -> new ResourceNotFoundException("Customers", "CustomerId", cId));
        if (product.getQuantity() >= cart.getQuantity()) {
            Customer productOwner = product.getCustomer();
            cart.setProductOwner(productOwner.getFullName());
            cart.setCustomer(customer);
            cart.setStatus(Status.ACTIVE);
            cart.setProductImage(product.getProductImage());
            cart.setProductName(product.getName());
            cart.setProductPrice(product.getPrice());
            cartRepo.save(cart);
            return "Successfully added in cart";
        } else {
            return "Product out of Stock";
        }
    }

    @Override
    public String deleteCart(Integer cartId) {
        Carts cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));
        cartRepo.delete(cart);
        return "Deleted successfully";
    }

    @Override
    public List<CartResponseDto> getAllCart(Integer cID) {
        Customer customer = customerRepo.findById(cID).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cID));
        List<Carts> carts = cartRepo.findByCustomerAndStatus(customer, Status.ACTIVE);
        return carts.stream().map(li -> modelMapper.map(li, CartResponseDto.class)).toList();
    }

    @Override
    public String deleteAll(Integer cId) {
        Customer customer = customerRepo.findById(cId).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", cId));
        List<Carts> cartList = cartRepo.findAllByCustomer(customer);
        cartRepo.deleteAll(cartList);
        return "Deleted successfully";
    }

    @Scheduled(cron = "${scheduled.task.cron.expression}")
    private void checkCartValidity() {
        List<Carts> cartList = cartRepo.findByStatus(Status.ACTIVE);
        for (Carts cart : cartList) {
            Product product = productRepo.findByNameAndCustomerFullName(cart.getProductName(), cart.getProductOwner());
            if (product.getQuantity() < cart.getQuantity()) {
                cart.setStatus(Status.INACTIVE);
                cartRepo.save(cart);
            }
        }
    }

    @Scheduled(cron = "${scheduled.task.cron.expression}")
    private void checkCartRestore() {
        List<Carts> cartList = cartRepo.findByStatus(Status.INACTIVE);
        for (Carts cart : cartList) {
            Product product = productRepo.findByNameAndCustomerFullName(cart.getProductName(), cart.getProductOwner());
            if (product.getQuantity() > cart.getQuantity()) {
                cart.setStatus(Status.ACTIVE);
                cartRepo.save(cart);
            }
        }
    }
}
