package com.dailycodebuffer.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallback")
    public String orderServiceFallBackMethod() {
        return "Order Service is down.";
    }

    @GetMapping("/productServiceFallback")
    public String productServiceFallBackMethod() {
        return "Product Service is down.";
    }

    @GetMapping("/paymentServiceFallback")
    public String paymentServiceFallBackMethod() {
        return "Payment Service is down.";
    }
}
