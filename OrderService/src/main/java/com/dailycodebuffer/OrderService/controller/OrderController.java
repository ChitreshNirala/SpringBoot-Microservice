package com.dailycodebuffer.OrderService.controller;


import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.services.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Placing order request");
        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order placed successfully with order id: {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
