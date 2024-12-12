package com.sparta.msa.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/products/{productId}")
    ProductResponse getProductById(@PathVariable UUID productId);
}