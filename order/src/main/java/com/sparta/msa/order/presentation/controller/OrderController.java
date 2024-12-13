package com.sparta.msa.order.presentation.controller;

import com.sparta.msa.order.application.dto.OrderDetailResponse;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.application.service.OrderService;
import com.sparta.msa.order.exception.CommonResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
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
    public ResponseEntity<OrderDetailResponse> updateOrder(
            @PathVariable UUID orderUUID,
            @Valid @RequestBody OrderRequest request) {
        OrderDetailResponse response = orderService.updateOrder(orderUUID, request);
        return ResponseEntity.ok(response);
    }

    // 주문 취소
    @PutMapping("/{orderUUID}/cancel")
    public ResponseEntity<CommonResponse<OrderDetailResponse>> cancelOrder(@PathVariable UUID orderUUID) {
        CommonResponse<OrderDetailResponse> response = orderService.cancelOrder(orderUUID);
        return ResponseEntity.ok(response);
    }

    // 주문 삭제
    @DeleteMapping("/{orderUUID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID) {
        orderService.deleteOrder(orderUUID);
        return ResponseEntity.noContent().build();
    }
}
