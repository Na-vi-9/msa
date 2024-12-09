package com.sparta.msa.order.controller;

import com.sparta.msa.order.dto.CreateOrderRequest;
import com.sparta.msa.order.dto.OrderResponse;
import com.sparta.msa.order.dto.UpdateOrderRequest;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.service.OrderService;
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
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            // 검증 로직: 수량이 0 이하인 경우 에러
            if (request.getQuantity() <= 0) {
                throw new CustomException(ErrorCode.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
            }

            OrderResponse response = orderService.createOrder(request);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ORDER_CREATION_FAILED, "주문 생성 중 문제가 발생했습니다.");
        }
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @RequestParam(required = false) String condition,
            Pageable pageable) {
        try {
            Page<OrderResponse> response = orderService.getOrders(condition, pageable);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "주문 목록 조회 중 문제가 발생했습니다.");
        }
    }

    // 주문 단건 조회
    @GetMapping("/{orderUUID}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable UUID orderUUID) {
        try {
            OrderResponse response = orderService.getOrderDetail(orderUUID);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND, String.format("주문 UUID [%s]를 찾을 수 없습니다.", orderUUID));
        }
    }

    // 주문 수정
    @PutMapping("/{orderUUID}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable UUID orderUUID,
            @RequestBody UpdateOrderRequest request) {
        try {
            // 검증 로직: 수량이 0 이하인 경우 에러
            if (request.getQuantity() <= 0) {
                throw new CustomException(ErrorCode.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
            }

            OrderResponse response = orderService.updateOrder(orderUUID, request);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ORDER_CANNOT_BE_UPDATED, String.format("주문 UUID [%s]를 수정할 수 없습니다.", orderUUID));
        }
    }

    // 주문 삭제
    @DeleteMapping("/{orderUUID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID) {
        try {
            orderService.deleteOrder(orderUUID);
            return ResponseEntity.noContent().build();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED, String.format("주문 UUID [%s]는 이미 취소된 상태입니다.", orderUUID));
        }
    }
}
