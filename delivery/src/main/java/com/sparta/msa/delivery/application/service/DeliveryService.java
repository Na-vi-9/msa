package com.sparta.msa.delivery.application.service;

import com.sparta.msa.delivery.application.dto.CreateDeliveryDto;
import com.sparta.msa.delivery.application.dto.CreateDeliveryResponse;
import com.sparta.msa.delivery.application.dto.UpdateDeliveryResponse;
import com.sparta.msa.delivery.application.dto.UpdateDeliveryDto;
import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.domain.repository.DeliveryRepository;
import com.sparta.msa.delivery.infrastructure.exception.CustomException;
import com.sparta.msa.delivery.infrastructure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public CreateDeliveryResponse createDelivery(CreateDeliveryDto dto) {

        Delivery delivery = Delivery.create(dto.getOrderUUID(), dto.getStatus(), dto.getDepartureHubUUID(), dto.getArrivalHubUUID(), dto.getDeliveryAddress(), dto.getReceiverName(), dto.getReceiverSlackId());
        return CreateDeliveryResponse.of(deliveryRepository.save(delivery));

    }

    @Transactional
    public UpdateDeliveryResponse updateDelivery(UUID deliveryUUID, UpdateDeliveryDto request) {

        return deliveryRepository.findByUuidAndIsDeletedFalse(deliveryUUID).map(delivery -> {
            delivery.update(request.getOrderUUID(), request.getStatus(), request.getDepartureHubUUID(), request.getArrivalHubUUID(), request.getDeliveryAddress(), request.getReceiverName(), request.getReceiverSlackId());
            return UpdateDeliveryResponse.of(delivery);
        }).orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

    }

    @Transactional
    public void deleteDelivery(UUID deliveryUUID, String deleteBy) {
        deliveryRepository.findByUuidAndIsDeletedFalse(deliveryUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND))
                .delete(deleteBy);
    }
}
