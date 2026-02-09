package com.dailycodebuffer.ProductService.services;


import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;

public interface ProductService {

    Long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long productId);
}
