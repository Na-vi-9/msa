package com.sparta.msa.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "product-service", url = "http://localhost:19092")
public interface ProductClient {

    @GetMapping("/products/{productUUID}")
    ProductInfo getProductById(@PathVariable UUID productUUID);

    @PutMapping("/products/{productUUID}/quantity")
    void updateProductQuantity(
            @PathVariable UUID productUUID,
            @RequestParam("quantityChange") int quantityChange,
            @RequestHeader("Authorization") String token
    );
}
