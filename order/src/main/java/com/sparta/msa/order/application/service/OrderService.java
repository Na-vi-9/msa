package com.sparta.msa.order.application.service;

import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성
    public OrderResponse createOrder(OrderRequest request) {
        UUID supplierCompanyId = request.getSupplierCompanyID();
        UUID receiverCompanyId = request.getReceiverCompanyID();
        UUID productId = request.getProductID();
        UUID deliveryId = UUID.randomUUID(); // 임시 UUID 생성

        // 값이 null인지 확인
        if (supplierCompanyId == null || receiverCompanyId == null || productId == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "필수 데이터가 누락되었습니다.");
        }

        try {
            Order order = Order.createOrder(request, supplierCompanyId, receiverCompanyId, productId, deliveryId);
            orderRepository.save(order);
            return new OrderResponse(order.getUuid(), order.getDeliveryId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ORDER_CREATION_FAILED, "DB 저장 중 문제가 발생했습니다.");
        }

    }

    // 주문 목록 조회
    public Page<OrderResponse> getOrders(String condition, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithCondition(condition, null, pageable);

        if (orders.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND, "주문 목록이 비어 있습니다.");
        }

        return orders.map(order -> new OrderResponse(order.getUuid(), order.getDeliveryId()));
    }

    // 주문 단건 조회
    public OrderResponse getOrderDetail(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return new OrderResponse(order.getUuid(), order.getDeliveryId());
    }

    // 주문 수정
    public OrderResponse updateOrder(UUID orderUUID, OrderRequest request) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.updateOrder(
                request.getSupplierCompanyID(),
                request.getReceiverCompanyID(),
                request.getProductID(),
                request.getQuantity(),
                request.getMemo(),
                "system"
        );

        Order updatedOrder = orderRepository.save(order);

        return new OrderResponse(updatedOrder.getUuid(), updatedOrder.getDeliveryId());
    }

    // 주문 삭제
    public void deleteOrder(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.delete("system");

        orderRepository.save(order);
    }
}
