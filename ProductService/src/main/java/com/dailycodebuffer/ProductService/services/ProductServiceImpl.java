package com.dailycodebuffer.ProductService.services;

import com.dailycodebuffer.ProductService.entity.Product;
import com.dailycodebuffer.ProductService.exception.ProductServiceCustomException;
import com.dailycodebuffer.ProductService.model.ProductRequest;
import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements  ProductService {

    @Autowired
    private ProductRepository productRepository; // Corrected the spelling of ProductRepository

    @Override
    public Long addProduct(ProductRequest productRequest) {
        log.info("Adding product.. ");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        productRepository.save(product); // Updated to use the corrected repository name
        log.info("Product Created...");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        log.info("Getting product for id: {}", productId);
         Product product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product not found for given id" ,"PRODUCT NOT FOUND" ));
        ProductResponse productResponse = new ProductResponse();
        copyProperties(product, productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reducing quantity {} for product id: {}", quantity, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product not found for given id" ,"PRODUCT NOT FOUND" ));
        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Product does not have sufficient quantity", "INSUFFICIENT QUANTITY");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated successfully...");
    }
}
