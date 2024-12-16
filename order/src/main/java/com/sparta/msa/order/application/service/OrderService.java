package com.sparta.msa.order.application.service;

import com.sparta.msa.order.application.dto.OrderDetailResponse;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.infrastructure.client.ProductClient;
import com.sparta.msa.order.infrastructure.client.ProductInfo;
import com.sparta.msa.order.infrastructure.repository.OrderRepository;
import com.sparta.msa.order.infrastructure.utils.AuthorizationUtils;
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
    private final ProductClient productClient;
    private final AuthorizationUtils authorizationUtils;

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String token) {
        // 토큰에서 username 추출
        String username = authorizationUtils.extractUsername(token);

        // 제품 정보 확인
        ProductInfo productResponse = productClient.getProductById(request.getProductUUID());
        if (productResponse == null || productResponse.getQuantity() <= 0) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // Order 생성
        UUID deliveryUUID = UUID.randomUUID();
        Order order = Order.createOrder(
                request,
                request.getSupplierCompanyUUID(),
                request.getReceiverCompanyUUID(),
                request.getProductUUID(),
                deliveryUUID,
                username // 생성자를 username으로 설정
        );

        // 저장
        orderRepository.save(order);

        // 응답 반환
        return new OrderResponse(order.getUuid(), order.getDeliveryUUID());
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderListResponse> getOrders(String condition, Pageable pageable) {
        return orderRepository.findAllWithCondition(condition, null, pageable)
                .map(OrderListResponse::new); // Order 객체를 전달
    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        return new OrderDetailResponse(order);
    }

    // 주문 수정 (권한 검증 및 username 추가)
    @Transactional
    public OrderDetailResponse updateOrder(UUID orderUUID, OrderRequest request, String token) {
        authorizationUtils.validateRole(token, "MASTER", "HUB_MANAGER");
        String username = authorizationUtils.extractUsername(token);

        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.updateOrder(request.getSupplierCompanyUUID(), request.getReceiverCompanyUUID(),
                request.getProductUUID(), request.getQuantity(), request.getMemo(), username);

        return new OrderDetailResponse(order);
    }

    // 주문 삭제 (권한 검증 및 username 추가)
    @Transactional
    public void deleteOrder(UUID orderUUID, String token) {
        authorizationUtils.validateRole(token, "MASTER", "HUB_MANAGER");
        String username = authorizationUtils.extractUsername(token);

        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.delete(username);
        orderRepository.save(order);
    }

    // 주문 취소
    @Transactional
    public OrderDetailResponse cancelOrder(UUID orderUUID, String token) {
        authorizationUtils.validateRole(token, "MASTER", "HUB_MANAGER", "USER");
        String username = authorizationUtils.extractUsername(token);

        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (order.isCanceled()) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        order.cancel(username);
        return new OrderDetailResponse(order);
    }
}
