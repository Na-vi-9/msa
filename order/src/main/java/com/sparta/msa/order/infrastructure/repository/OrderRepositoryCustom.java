package com.sparta.msa.order.infrastructure.repository;

import com.sparta.msa.order.application.dto.OrderListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderListResponse> findProductsWithCondition(String condition, String keyword, Pageable pageable);
}
