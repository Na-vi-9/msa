package com.sparta.msa.order.application.service;

import com.sparta.msa.order.application.dto.OrderDetailResponse;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CommonResponse;
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
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        UUID deliveryUUID = UUID.randomUUID(); // 임시 UUID 생성

        try {
            Order order = Order.createOrder(
                    request,
                    request.getSupplierCompanyUUID(),
                    request.getReceiverCompanyUUID(),
                    request.getProductUUID(),
                    deliveryUUID
            );
            orderRepository.save(order);
            return new OrderResponse(order.getUuid(), order.getDeliveryUUID());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ORDER_CREATION_FAILED);
        }
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderListResponse> getOrders(String condition, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllWithCondition(condition, null, pageable);

        if (orders.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return orders.map(order -> OrderListResponse.builder()
                .uuid(order.getUuid())
                .supplierCompanyUUID(order.getSupplierCompanyUUID())
                .receiverCompanyUUID(order.getReceiverCompanyUUID())
                .productUUID(order.getProductUUID())
                .quantity(order.getQuantity())
                .memo(order.getMemo())
                .build());
    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return OrderDetailResponse.builder()
                .supplierCompanyUUID(order.getSupplierCompanyUUID())
                .receiverCompanyUUID(order.getReceiverCompanyUUID())
                .productUUID(order.getProductUUID())
                .quantity(order.getQuantity())
                .memo(order.getMemo())
                .deliveryUUID(order.getDeliveryUUID())
                .build();
    }

    // 주문 수정
    @Transactional
    public OrderDetailResponse updateOrder(UUID orderUUID, OrderRequest request) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.updateOrder(
                request.getSupplierCompanyUUID(),
                request.getReceiverCompanyUUID(),
                request.getProductUUID(),
                request.getQuantity(),
                request.getMemo(),
                "system"
        );

        Order updatedOrder = orderRepository.save(order);

        return OrderDetailResponse.builder()
                .supplierCompanyUUID(updatedOrder.getSupplierCompanyUUID())
                .receiverCompanyUUID(updatedOrder.getReceiverCompanyUUID())
                .productUUID(updatedOrder.getProductUUID())
                .quantity(updatedOrder.getQuantity())
                .memo(updatedOrder.getMemo())
                .deliveryUUID(updatedOrder.getDeliveryUUID())
                .build();
    }

    // 주문 취소
    @Transactional
    public CommonResponse<OrderDetailResponse> cancelOrder(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (order.isCanceled()) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        order.cancel("system");
        Order canceledOrder = orderRepository.save(order);

        OrderDetailResponse response = OrderDetailResponse.builder()
                .supplierCompanyUUID(canceledOrder.getSupplierCompanyUUID())
                .receiverCompanyUUID(canceledOrder.getReceiverCompanyUUID())
                .productUUID(canceledOrder.getProductUUID())
                .quantity(canceledOrder.getQuantity())
                .memo(canceledOrder.getMemo())
                .deliveryUUID(canceledOrder.getDeliveryUUID())
                .build();

        return CommonResponse.ofSuccess(response);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.delete("system");

        orderRepository.save(order);
    }
}
