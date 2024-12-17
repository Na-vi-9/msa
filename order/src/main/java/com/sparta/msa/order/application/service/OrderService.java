package com.sparta.msa.order.application.service;

import com.sparta.msa.order.application.dto.OrderDetailResponse;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.infrastructure.client.CompanyClient;
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
    private final CompanyClient companyClient;
    private final AuthorizationUtils authorizationUtils;

    // 주문 생성 (COMMON)
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String token) {
        String username = authorizationUtils.extractUsername(token);

        // 제품 정보 확인
        ProductInfo product = productClient.getProductById(request.getProductUUID());
        if (product == null || product.getQuantity() < request.getQuantity()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 회사 존재 여부 확인
        validateCompanyExists(request.getSupplierCompanyUUID());
        validateCompanyExists(request.getReceiverCompanyUUID());

        // 상품 수량 감소
        productClient.updateProductQuantity(request.getProductUUID(), -request.getQuantity(), token);

        // 주문 생성
        UUID deliveryUUID = UUID.randomUUID();
        Order order = Order.createOrder(
                request,
                request.getSupplierCompanyUUID(),
                request.getReceiverCompanyUUID(),
                request.getProductUUID(),
                deliveryUUID,
                username
        );

        orderRepository.save(order);
        return new OrderResponse(order.getUuid(), order.getDeliveryUUID());
    }

    // 주문 목록 조회 (COMMON)
    @Transactional(readOnly = true)
    public Page<OrderListResponse> getOrders(String condition, String keyword, Pageable pageable) {
        return orderRepository.findProductsWithCondition(condition, keyword, pageable);
    
    }

    // 주문 단건 조회 (COMMON)
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderUUID) {
        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        return new OrderDetailResponse(order);
    }

    // 주문 수정 (MASTER, HUB_MANAGER)
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

    // 주문 삭제 (MASTER, HUB_MANAGER)
    @Transactional
    public void deleteOrder(UUID orderUUID, String token) {
        authorizationUtils.validateRole(token, "MASTER", "HUB_MANAGER");
        String username = authorizationUtils.extractUsername(token);

        Order order = orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.delete(username);
        orderRepository.save(order);
    }

    private void validateCompanyExists(UUID companyUUID) {
        if (!companyClient.checkCompanyExists(companyUUID)) {
            throw new CustomException(ErrorCode.COMPANY_NOT_FOUND);
        }
    }
}
