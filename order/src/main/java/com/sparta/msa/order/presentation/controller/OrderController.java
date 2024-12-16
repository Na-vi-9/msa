package com.sparta.msa.order.presentation.controller;

import com.sparta.msa.order.application.dto.OrderDetailResponse;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
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
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request,
                                                     @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.createOrder(request, token));
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getOrders(@RequestParam(required = false) String condition,
                                                             Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(condition, pageable));
    }

    // 주문 단건 조회
    @GetMapping("/{orderUUID}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable UUID orderUUID) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderUUID));
    }

    // 주문 수정
    @PutMapping("/{orderUUID}")
    public ResponseEntity<OrderDetailResponse> updateOrder(@PathVariable UUID orderUUID,
                                                           @RequestBody OrderRequest request,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.updateOrder(orderUUID, request, token));
    }

    // 주문 삭제
    @DeleteMapping("/{orderUUID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID,
                                            @RequestHeader("Authorization") String token) {
        orderService.deleteOrder(orderUUID, token);
        return ResponseEntity.noContent().build();
    }

    // 주문 취소
    @PutMapping("/{orderUUID}/cancel")
    public ResponseEntity<OrderDetailResponse> cancelOrder(@PathVariable UUID orderUUID,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.cancelOrder(orderUUID, token));
    }
}
