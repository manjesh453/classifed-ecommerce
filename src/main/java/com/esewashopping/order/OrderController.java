package com.esewashopping.order;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order/")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/purchase-all-cart/{cId}")
    public String purchaseAllCart(@RequestBody OrderRequestDto requestDto, @PathVariable Integer cId) {
        return orderService.orderItem(requestDto, cId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deliverd/{orderId}")
    public String changeStatus(@PathVariable Integer orderId) {
        return orderService.changeStatus(orderId);
    }

    @GetMapping("/get-pending-orders/{cId}")
    public List<OrderResponseDto> getPaymentPendingOrder(@PathVariable Integer cId) {
        return orderService.beforePayment(cId);
    }

    @GetMapping("/get-success-0rder/{cId}")
    public List<OrderResponseDto> getPaymentVerifiedOrder(@PathVariable Integer cId) {
        return orderService.getOrderListById(cId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-orders")
    public List<OrderResponseDto> getAllSuccessOrder() {
        return orderService.getAllApproveOrder();
    }

    @GetMapping("/success-payment/{cId}")
    public String afterPaymentSuccess(@PathVariable Integer cId) throws IOException {
        return orderService.afterPayment(cId);
    }
    @PostMapping("/purchase-single-cart/{cartId}/{cId}")
    public String singleCartPurchase(@RequestBody OrderRequestDto orderRequestDto,@PathVariable Integer cartId,@PathVariable Integer cId){
        return orderService.orderSingleCartItem(orderRequestDto,cartId,cId);
    }
}
