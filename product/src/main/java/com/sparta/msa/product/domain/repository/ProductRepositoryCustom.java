package com.sparta.msa.product.domain.repository;

import com.sparta.msa.product.application.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductResponse> findProductsWithCondition(String condition, String keyword, Pageable pageable);
}
