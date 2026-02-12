package com.dailycodebuffer.PaymentService.services;

import com.dailycodebuffer.PaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
