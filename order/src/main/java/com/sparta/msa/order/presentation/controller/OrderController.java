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

    // 주문 생성 (COMMON)
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request,
                                                     @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.createOrder(request, token));
    }

    // 주문 목록 조회 (COMMON)
    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getOrders(@RequestParam(required = false) String condition,
                                                             Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(condition, pageable));
    }

    // 주문 단건 조회 (COMMON)
    @GetMapping("/{orderUUID}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable UUID orderUUID) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderUUID));
    }

    // 주문 수정 (MASTER, HUB_MANAGER)
    @PutMapping("/{orderUUID}")
    public ResponseEntity<OrderDetailResponse> updateOrder(@PathVariable UUID orderUUID,
                                                           @RequestBody OrderRequest request,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.updateOrder(orderUUID, request, token));
    }

    // 주문 삭제 (MASTER, HUB_MANAGER)
    @DeleteMapping("/{orderUUID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID,
                                            @RequestHeader("Authorization") String token) {
        orderService.deleteOrder(orderUUID, token);
        return ResponseEntity.noContent().build();
    }
}
