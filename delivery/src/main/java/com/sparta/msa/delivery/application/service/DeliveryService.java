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

        Delivery delivery = Delivery.create(dto.getOrderUUID(), dto.getStatus(), dto.getDepartureHubUUID(), dto.getArrivalHubUUID(), dto.getDeliveryAddress(), dto.getReceiverName(), dto.getReceiverSlackId());
        return CreateDeliveryResponse.of(deliveryRepository.save(delivery));

    }
}
