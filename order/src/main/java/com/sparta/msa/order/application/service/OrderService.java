package com.sparta.msa.order.application.service;

import com.sparta.msa.order.application.dto.OrderDetailResponse;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.application.dto.OrderRequest;
import com.sparta.msa.order.application.dto.OrderResponse;
import com.sparta.msa.order.domain.model.Order;
import com.sparta.msa.order.exception.CommonResponse;
import com.sparta.msa.order.exception.CustomException;
import com.sparta.msa.order.exception.ErrorCode;
import com.sparta.msa.order.infrastructure.client.*;
import com.sparta.msa.order.infrastructure.repository.OrderRepository;
import com.sparta.msa.order.infrastructure.utils.AuthorizationUtils;
import com.sparta.msa.order.presentation.request.GeminiClientRequestDto;
import com.sparta.msa.order.presentation.response.AiMessageCreateResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserFeignClient userFeignClient;
    private final AuthorizationUtils authorizationUtils;
    private final AiFeignClient aiFeignClient;
    private final HubRouteFeignClient hubRouteFeignClient;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String token) {
        String username = authorizationUtils.extractUsername(token);

        // AI 서비스 호출
        AiMessageCreateResponseDto aiResponse = generateFinalDeadline(request, token);

        // 주문 생성
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

        return new OrderResponse(order.getUuid(), order.getDeliveryUUID());
    }


    @Transactional(readOnly = true)
    public Page<OrderListResponse> getOrders(String condition, Pageable pageable) {
        return orderRepository.findAllWithCondition(condition, null, pageable)
                .map(OrderListResponse::new);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderUUID) {
        Order order = findOrderById(orderUUID);
        return new OrderDetailResponse(order);
    }

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

    @Transactional
    public void deleteOrder(UUID orderUUID, String token) {
        validateUserRole(token, "MASTER", "HUB_MANAGER");

        Order order = findOrderById(orderUUID);
        String username = authorizationUtils.extractUsername(token);

        order.delete(username);
        orderRepository.save(order);
    }

    @Transactional
    public OrderDetailResponse cancelOrder(UUID orderUUID, String token) {
        validateUserRole(token, "MASTER", "HUB_MANAGER");

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

    private HubRouteInfoResponse validateHubAvailability(UUID arrival, UUID departure) {
        HubRouteInfoRequest hubRouteInfoRequest = HubRouteInfoRequest.builder()
                                                    .arrivalHubUUID(arrival)
                                                    .departureHubUUID(departure)
                                                    .build();
        HubRouteInfoResponse hubRouteInfo =  hubRouteFeignClient.getHubRouteId(hubRouteInfoRequest).data();
        if (hubRouteInfo == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        return hubRouteInfo;
    }

    private HubInfoResponse getHub(UUID hubUUID) {
//        HubInfoRequest hubInfoRequest = HubInfoRequest.builder().hubUUID(hubUUID).build();
        HubInfoResponse hubInfoResponse = hubRouteFeignClient.getHub(hubUUID).data();
        if (hubInfoResponse == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        return hubInfoResponse;
    }


    private AiMessageCreateResponseDto generateFinalDeadline(OrderRequest request, String token) {
        // 로그 추가
        System.out.println("Generating final deadline with request: " + request);

        ProductInfo productInfo = validateProductAvailability(request.getProductUUID());
        String productName = productInfo.getName();

        // 1. 경로 아이디 가져오기 (출발 도착 허브id로)

        HubRouteInfoResponse hubRouteInfoResponse = validateHubAvailability(request.getReceiverCompanyUUID(), request.getSupplierCompanyUUID());
        System.out.println("###########");
        System.out.println(hubRouteInfoResponse.getArrivalUUID());
        System.out.println(hubRouteInfoResponse.getDepartureUUID());

        // 2. 주는애
        HubInfoResponse hubInfoResponseSupplier = getHub(request.getReceiverCompanyUUID());
        System.out.println(hubInfoResponseSupplier.getHubName());
        System.out.println(hubInfoResponseSupplier.getHubName());

        // 3. 받는애
        HubInfoResponse hubInfoResponseReceiver = getHub(request.getSupplierCompanyUUID());
        System.out.println(hubInfoResponseReceiver.getHubName());
        System.out.println(hubInfoResponseReceiver.getHubName());

        String prompt =
                "상품: " + productName +
                ", 수량: " + request.getQuantity() +
                ", 배송 마감일 : " + request.getMemo() +
                ", 발송지: " + hubInfoResponseSupplier.getHubAddress() +
                ", 도착지: " + hubInfoResponseReceiver.getHubAddress() +
                ", 배송 시간 : " +  hubRouteInfoResponse.getDurationMin();

//      String additionalMessage = ", 위 내용을 기반으로 몇월 며칠 오전/오후 몇시까지 최종 발송 시한을 도출해줘."
        String additionalMessage ="발송지에서 도착지까지 차로 배송을 할거야, " +
                "배송 준비 시간은 1일이야" +
                "배송마감일에 도착을 하려면 몇월 며칠 오전/오후에 출발해야하는지 너가 계산해서 알려줄래?";

        // 로그 추가
        System.out.println("Generated prompt: " + prompt);

        GeminiClientRequestDto aiRequest = GeminiClientRequestDto.create(prompt, additionalMessage);

        // 로그 추가
        System.out.println("Generated AI Request: " + aiRequest);

        String jwtToken = authorizationUtils.extractToken(token);
        AiMessageCreateResponseDto response = aiFeignClient.createAiMessage(jwtToken, aiRequest);

        // 로그 추가
        System.out.println("AI Response: " + response);

        return response;
    }



//    private void sendSlackNotification(String finalDeadline) {
//        try {
//            String message = "최종 발송 시한: " + finalDeadline;
//            slackService.sendMessage(message);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Slack 알림 전송 실패", e);
//        }
//    }



    private Order findOrderById(UUID orderUUID) {
        return orderRepository.findByUuidAndIsDeletedFalse(orderUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private void validateUserRole(String token, String... roles) {
        authorizationUtils.validateRole(token, roles);
    }
}
