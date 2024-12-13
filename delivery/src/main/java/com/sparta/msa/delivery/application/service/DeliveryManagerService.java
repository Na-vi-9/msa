package com.sparta.msa.delivery.application.service;

import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.infrastructure.exception.CustomException;
import com.sparta.msa.delivery.infrastructure.exception.ErrorCode;
import com.sparta.msa.delivery.infrastructure.repository.DeliveryManagerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerJpaRepository deliveryManagerJpaRepository;

    @Transactional
    public DeliveryManagerResponse addDeliveryManager(DeliveryManagerRequest request) {

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrder();
        int nextOrder = (lastOrder != null) ? lastOrder + 1 : 1;

        DeliveryManager deliveryManager = DeliveryManager.create(
                request.getHubUUID(),
                request.getSlackId(),
                request.getType(),
                nextOrder,
                request.getUsername()
        );

        deliveryManagerJpaRepository.save(deliveryManager);
        return new DeliveryManagerResponse(deliveryManager);
    }

    @Transactional
    public DeliveryManagerResponse assignDeliveryManager() {
        DeliveryManager deliveryManager = deliveryManagerJpaRepository.findTopByIsDeletedFalseOrderByDeliveryOrderAsc()
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_MANAGER_NOT_FOUND));

        Integer lastOrder = deliveryManagerJpaRepository.findLastDeliveryOrder();
        deliveryManager.updateDeliveryOrder((lastOrder != null) ? lastOrder + 1 : 1);

        deliveryManagerJpaRepository.save(deliveryManager);
        return new DeliveryManagerResponse(deliveryManager);
    }

}
