package com.sparta.msa.order.application.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.msa.order.application.dto.*;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.infrastructure.client.AiFeignClient;
import com.sparta.msa.order.infrastructure.client.ProductClient;
import com.sparta.msa.order.infrastructure.client.ProductInfo;
import com.sparta.msa.order.infrastructure.repository.OrderRepository;
import com.sparta.msa.order.infrastructure.utils.AuthorizationUtils;
import com.sparta.msa.order.presentation.request.AiMessageRequestDto;
import com.sparta.msa.order.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.order.presentation.response.AiMessageCreateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final AuthorizationUtils authorizationUtils;
    private final AiFeignClient aiFeignClient;
    private final SlackService slackService;

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String token) {
        String username = authorizationUtils.extractUsername(token);

        ProductInfo product = validateProductAvailability(request.getProductUUID());
        AiMessageCreateResponseDto aiResponse = generateFinalDeadline(request);

        Order order = Order.createOrder(
                request,
                request.getSupplierCompanyUUID(),
                request.getReceiverCompanyUUID(),
                request.getProductUUID(),
                UUID.randomUUID(),
                username,
                aiResponse.getContent()
        );

        orderRepository.save(order);

        // Slack 알림 전송
        sendSlackNotification(username, aiResponse.getContent());

        return new OrderResponse(order.getUuid(), order.getDeliveryUUID());
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderListResponse> getOrders(String condition, Pageable pageable) {
        return orderRepository.findAllWithCondition(condition, null, pageable)
                .map(OrderListResponse::new);
    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderUUID) {
        Order order = findOrderById(orderUUID);
        return new OrderDetailResponse(order);
    }

    // 주문 수정
    @Transactional
    public OrderDetailResponse updateOrder(UUID orderUUID, OrderRequest request, String token) {
        validateUserRole(token, "MASTER", "HUB_MANAGER");

        Order order = findOrderById(orderUUID);
        String username = authorizationUtils.extractUsername(token);

        order.updateOrder(
                request.getSupplierCompanyUUID(),
                request.getReceiverCompanyUUID(),
                request.getProductUUID(),
                request.getQuantity(),
                request.getMemo(),
                username
        );

        return new OrderDetailResponse(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(UUID orderUUID, String token) {
        validateUserRole(token, "MASTER", "HUB_MANAGER");

        Order order = findOrderById(orderUUID);
        String username = authorizationUtils.extractUsername(token);

        order.delete(username);
        orderRepository.save(order);
    }

    // 주문 취소
    @Transactional
    public OrderDetailResponse cancelOrder(UUID orderUUID, String token) {
        validateUserRole(token, "MASTER", "HUB_MANAGER", "USER");

        Order order = findOrderById(orderUUID);

        if (order.isCanceled()) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        String username = authorizationUtils.extractUsername(token);
        order.cancel(username);

        return new OrderDetailResponse(order);
    }

    // 유틸리티 메서드
    private ProductInfo validateProductAvailability(UUID productUUID) {
        ProductInfo product = productClient.getProductById(productUUID);
        if (product == null || product.getQuantity() <= 0) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return product;
    }

    private AiMessageCreateResponseDto generateFinalDeadline(OrderRequest request) {
        // Gemini 요청 DTO 생성
        String prompt = "상품: " + request.getProductUUID() +
                ", 수량: " + request.getQuantity() +
                ", 요청사항: " + request.getMemo() +
                ", 발송지: " + request.getSupplierCompanyUUID() +
                ", 도착지: " + request.getReceiverCompanyUUID();

        String additionalMessage = ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘.";

        // GeminiClientRequestDto 생성
        GeminiClientRequestDto aiRequest = GeminiClientRequestDto.create(prompt, additionalMessage);

        // 로그로 확인
        System.out.println("Generated Request: " + aiRequest);

        // FeignClient 호출
        return aiFeignClient.createAiMessage(aiRequest);
    }





    private void sendSlackNotification(String username, String finalDeadline) {
        try {
            slackService.sendMessage(username, "최종 발송 시한: " + finalDeadline);
        } catch (IOException e) {
            throw new RuntimeException("Slack 알림 전송 실패", e);
        }
    }


    private Order findOrderById(UUID orderUUID) {
        return orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private void validateUserRole(String token, String... roles) {
        authorizationUtils.validateRole(token, roles);
    }
}
