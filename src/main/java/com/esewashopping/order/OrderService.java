package com.esewashopping.order;

import org.aspectj.weaver.ast.Or;

import java.io.IOException;
import java.util.List;

public interface OrderService {

String orderItem(OrderRequestDto requestDto , Integer cId);

String changeStatus(Integer orderId);

String  afterPayment(Integer cId) throws IOException;

List<OrderResponseDto> getOrderListById(Integer cID);

List<OrderResponseDto> getAllApproveOrder();

List<OrderResponseDto> beforePayment(Integer cID);

String orderSingleCartItem(OrderRequestDto requestDto ,Integer cartId,Integer cId);


}
