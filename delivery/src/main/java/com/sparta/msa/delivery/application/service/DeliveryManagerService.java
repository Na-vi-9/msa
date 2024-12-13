package com.sparta.msa.delivery.application.service;

import com.querydsl.core.BooleanBuilder;
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

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrder().orElse(0);
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
    public DeliveryManagerResponse assignDeliveryManager() {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository.findTopByIsDeletedFalseOrderByDeliveryOrderAsc()
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrder().orElse(0);
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
    public List<DeliveryManagerResponse> getDeliveryManagers(String condition, String keyword) {
        return deliveryManagerJpaRepository.search(condition, keyword)
                .stream()
                .map(DeliveryManagerResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeliveryManagerResponse getDeliveryManagerDetail(String username) {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배송 담당자입니다."));

        return new DeliveryManagerResponse(deliveryManager);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryManagerResponse> searchDeliveryManagers(
            com.querydsl.core.types.Predicate predicate, int page, int size, String sort) {
        size = (size == 10 || size == 30 || size == 50) ? size : 10;

        Sort sorting = sort.equals("updatedAt")
                ? Sort.by(Sort.Direction.DESC, "updatedAt")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sorting);

        BooleanBuilder builder = new BooleanBuilder(predicate);
        builder.and(QDeliveryManager.deliveryManager.isDeleted.eq(false));

        return deliveryManagerJpaRepository.findAll(builder, pageable)
                .map(DeliveryManagerResponse::new);
    }



}