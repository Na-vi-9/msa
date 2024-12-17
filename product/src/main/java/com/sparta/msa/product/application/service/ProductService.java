package com.sparta.msa.product.application.service;

import com.sparta.msa.product.application.dto.CreateProductRequest;
import com.sparta.msa.product.application.dto.ProductResponse;
import com.sparta.msa.product.application.dto.UpdateProductRequest;
import com.sparta.msa.product.domain.model.Product;
import com.sparta.msa.product.domain.repository.ProductRepository;
import com.sparta.msa.product.exception.CustomException;
import com.sparta.msa.product.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 생성
    public ProductResponse createProduct(CreateProductRequest request, String createdBy) {
        Product product = Product.create(request, createdBy);
        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    // 상품 목록 조회
    public Page<ProductResponse> getProducts(String condition, String keyword, Pageable pageable) {
        Page<Product> products = productRepository.findAllWithCondition(condition, keyword, pageable);

        if (products.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return products.map(ProductResponse::new);
    }

    // 상품 단일 조회
    public ProductResponse getProduct(UUID productUUID) {
        Product product = productRepository.findByUuidAndIsDeletedFalse(productUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        return new ProductResponse(product);
    }

    // 상품 수정
    public ProductResponse updateProduct(UUID productUUID, UpdateProductRequest request, String updatedBy) {
        Product product = productRepository.findByUuidAndIsDeletedFalse(productUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        product.update(request, updatedBy);
        Product updatedProduct = productRepository.save(product);
        return new ProductResponse(updatedProduct);
    }

    // 상품 삭제
    public void deleteProduct(UUID productUUID, String deletedBy) {
        Product product = productRepository.findByUuidAndIsDeletedFalse(productUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        product.delete(deletedBy);
        productRepository.save(product);
    }
}
