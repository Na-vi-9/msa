package com.sparta.msa.order.application.service;

import com.sparta.msa.order.application.dto.CreateOrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.application.dto.UpdateOrderRequest;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성
    public OrderResponse createOrder(CreateOrderRequest request) {
        // 임시값
        Long supplierCompanyId = 1L;
        Long receiverCompanyId = 2L;
        Long productId = 3L;
        Long deliveryId = System.currentTimeMillis(); // 임시 고유값

        // 주문 엔티티 생성
        Order order = Order.createOrder(request, supplierCompanyId, receiverCompanyId, productId, deliveryId);
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(savedOrder.getUuid(), UUID.randomUUID()); // deliveryUUID는 임시로 생성
    }

    // 주문 목록 조회
    public Page<OrderResponse> getOrders(String condition, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithCondition(condition, null, pageable);

        if (orders.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND, "주문 목록이 비어 있습니다.");
        }

        return orders.map(order -> new OrderResponse(order.getUuid(), UUID.randomUUID())); // deliveryUUID는 임시로 생성
    }

    // 주문 단건 조회
    public OrderResponse getOrderDetail(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        return new OrderResponse(order.getUuid(), UUID.randomUUID()); // deliveryUUID는 임시로 생성
    }

    // 주문 수정
    public OrderResponse updateOrder(UUID orderUUID, UpdateOrderRequest request) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.setSupplierCompanyId(1L); // 임시값
        order.setReceiverCompanyId(2L); // 임시값
        order.setProductId(3L); // 임시값
        order.setQuantity(request.getQuantity());
        order.setMemo(request.getMemo());
        order.setUpdatedBy("system");

        Order updatedOrder = orderRepository.save(order);
        return new OrderResponse(updatedOrder.getUuid(), UUID.randomUUID()); // deliveryUUID는 임시로 생성
    }

    // 주문 삭제
    public void deleteOrder(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        order.delete("system"); // 삭제자 (임시값)
        orderRepository.save(order);
    }
}
