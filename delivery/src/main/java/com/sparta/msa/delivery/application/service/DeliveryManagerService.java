package com.sparta.msa.delivery.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.domain.model.DeliveryManagerType;
import com.sparta.msa.delivery.domain.model.QDeliveryManager;
import com.sparta.msa.delivery.infrastructure.exception.CustomException;
import com.sparta.msa.delivery.infrastructure.exception.ErrorCode;
import com.sparta.msa.delivery.infrastructure.repository.DeliveryManagerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerJpaRepository deliveryManagerJpaRepository;

    @Transactional
    public DeliveryManagerResponse addDeliveryManager(DeliveryManagerRequest request) {
        if (request.getType() == DeliveryManagerType.COMPANY_MANAGER && request.getHubUUID() == null) {
            throw new IllegalArgumentException("소속 허브 ID는 필수입니다.");
        }

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrder();
        lastOrder = (lastOrder != null) ? lastOrder : 0;
        int nextOrder = lastOrder + 1;

        DeliveryManager deliveryManager = DeliveryManager.create(
                request.getHubUUID(),
                request.getType(),
                request.getUsername(),
                nextOrder
        );

        deliveryManagerJpaRepository.save(deliveryManager);
        return new DeliveryManagerResponse(deliveryManager);
    }

    @Transactional
    public DeliveryManagerResponse assignHubDeliveryManager() {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository
                .findTopByTypeAndIsDeletedFalseOrderByDeliveryOrderAsc(DeliveryManagerType.HUB_MANAGER)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrderByType(DeliveryManagerType.HUB_MANAGER);
        lastOrder = (lastOrder != null) ? lastOrder : 0;

        deliveryManager.updateDeliveryOrder(lastOrder + 1);
        deliveryManagerJpaRepository.save(deliveryManager);

        return new DeliveryManagerResponse(deliveryManager);
    }


    @Transactional
    public DeliveryManagerResponse assignCompanyDeliveryManager(UUID hubUUID) {
        // 로그 추가
        System.out.println("Assigning company delivery manager for hubUUID: " + hubUUID);

        Optional<DeliveryManager> deliveryManagerOpt = deliveryManagerJpaRepository
                .findTopByTypeAndHubUUIDAndIsDeletedFalseOrderByDeliveryOrderAsc(DeliveryManagerType.COMPANY_MANAGER, hubUUID);

        // 추가 로그: 조회된 결과 확인
        System.out.println("Delivery Manager found: " + deliveryManagerOpt);

        DeliveryManager deliveryManager = deliveryManagerOpt
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrderByTypeAndHubUUID(DeliveryManagerType.COMPANY_MANAGER, hubUUID);
        lastOrder = (lastOrder != null) ? lastOrder : 0;

        deliveryManager.updateDeliveryOrder(lastOrder + 1);
        deliveryManagerJpaRepository.save(deliveryManager);

        return new DeliveryManagerResponse(deliveryManager);
    }




    @Transactional
    public DeliveryManagerResponse updateDeliveryManager(String username, DeliveryManagerRequest request) {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

        if (request.getType() == DeliveryManagerType.COMPANY_MANAGER && request.getHubUUID() == null) {
            throw new IllegalArgumentException("소속 허브 ID는 필수입니다.");
        }

        deliveryManager.update(
                request.getHubUUID(),
                request.getType(),
                deliveryManager.getDeliveryOrder()
        );

        return new DeliveryManagerResponse(deliveryManager);
    }

    @Transactional
    public void deleteDeliveryManager(String username) {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

        deliveryManager.delete("system");
    }

    @Transactional(readOnly = true)
    public Page<DeliveryManagerResponse> getDeliveryManagers(Predicate predicate, Pageable pageable) {
        return deliveryManagerJpaRepository.findWithPredicate(predicate, pageable)
                .map(DeliveryManagerResponse::new);
    }


    @Transactional(readOnly = true)
    public DeliveryManagerResponse getDeliveryManagerDetail(String username) {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

        return new DeliveryManagerResponse(deliveryManager);
    }

}
