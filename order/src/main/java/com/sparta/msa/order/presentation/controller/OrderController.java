package com.sparta.msa.order.presentation.controller;

import com.sparta.msa.order.application.dto.*;
import com.sparta.msa.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getOrders(
            @RequestParam(required = false) String condition,
            Pageable pageable) {
        Page<OrderListResponse> response = orderService.getOrders(condition, pageable);
        return ResponseEntity.ok(response);
    }

    // 주문 단건 조회
    @GetMapping("/{orderUUID}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable UUID orderUUID) {
        OrderDetailResponse response = orderService.getOrderDetail(orderUUID);
        return ResponseEntity.ok(response);
    }

    // 주문 수정
    @PutMapping("/{orderUUID}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable UUID orderUUID,
            @RequestBody OrderRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        OrderResponse response = orderService.updateOrder(orderUUID, request);
        return ResponseEntity.ok(response);
    }

    // 주문 삭제
    @DeleteMapping("/{orderUUID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID) {
        orderService.deleteOrder(orderUUID);
        return ResponseEntity.noContent().build();
    }
}
