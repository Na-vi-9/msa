package com.sparta.msa.product.presentation.controller;


import com.sparta.msa.product.application.dto.CreateProductRequest;
import com.sparta.msa.product.application.dto.ProductResponse;
import com.sparta.msa.product.application.dto.UpdateProductRequest;
import com.sparta.msa.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 생성
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<ProductResponse> response = productService.getProducts(condition, keyword, pageable);
        return ResponseEntity.ok(response);
    }

    // 상품 단일 조회
    @GetMapping("/{productUUID}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productUUID) {
        ProductResponse response = productService.getProduct(productUUID);
        return ResponseEntity.ok(response);
    }

    // 상품 수정
    @PutMapping("/{productUUID}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productUUID,
            @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.updateProduct(productUUID, request);
        return ResponseEntity.ok(response);
    }

    // 상품 삭제
    @DeleteMapping("/{productUUID}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productUUID) {
        productService.deleteProduct(productUUID);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productUUID}/quantity")
    public ResponseEntity<Void> updateProductQuantity(@PathVariable UUID productUUID,
                                                      @RequestParam("quantityChange") int quantityChange,
                                                      @RequestHeader("Authorization") String token) {
        // 권한 검증 로직 확인 필요
        productService.updateProductQuantity(productUUID, quantityChange);
        return ResponseEntity.ok().build();
    }

}
