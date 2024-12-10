package com.sparta.msa.delivery.application.service;

import com.sparta.msa.delivery.application.dto.CreateDeliveryDto;
import com.sparta.msa.delivery.application.dto.CreateDeliveryResponse;
import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public CreateDeliveryResponse createDelivery(CreateDeliveryDto dto) {
        Long orderId = 1L;
        Long departureHubId = 1L;
        Long arrivalHubId = 1L;

        Delivery delivery = Delivery.create(orderId, dto.getStatus(), departureHubId, arrivalHubId, dto.getDeliveryAddress(), dto.getReceiverName(), dto.getReceiverSlackId());
        return CreateDeliveryResponse.of(deliveryRepository.save(delivery));

    }
}
