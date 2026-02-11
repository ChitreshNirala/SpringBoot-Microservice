package com.dailycodebuffer.OrderService.services;

import com.dailycodebuffer.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
