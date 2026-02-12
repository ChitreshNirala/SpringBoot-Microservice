package com.dailycodebuffer.OrderService.services;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.external.exception.CustomException;
import com.dailycodebuffer.OrderService.external.request.PaymentRequest;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.OrderResponse;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order entity - save the data with status order created
        //product service - block the products (reduce the quantity)
        //payment service - make payment --success-- complete, else cancel the order
        log.info("Placing order request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating order with status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .build();
        order = orderRepository.save(order);
        log.info("Calling payment service to process payment for order id: {}", order.getId());
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .amount(orderRequest.getTotalAmount())
                .paymentMode(orderRequest.getPaymentMode())
                .build();
        String orderStatus = null;
        try
        {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Order status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error Occured in Payment. Changing Order status to PAYMENT_FAILLED");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order saved successfully with id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for order id: {}", orderId);
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new CustomException("Order not found with id: " + orderId,"NOT_FOUND", 404));

        log.info("Invoking Product service to fetch product details for product id: {}", order.getProductId());
        ProductResponse productResponse =
                restTemplate.getForObject("http://PRODUCT-SERVICE/products/" + order.getProductId(), ProductResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productId(productResponse.getProductId())
                .productName(productResponse.getProductName())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .build();

        return orderResponse;
    }
}
