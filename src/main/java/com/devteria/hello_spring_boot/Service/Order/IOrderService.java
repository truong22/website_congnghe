package com.devteria.hello_spring_boot.Service.Order;

import com.devteria.hello_spring_boot.dto.OrderDto;

import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
