package com.sparta.msa.delivery.domain.repository;

import com.sparta.msa.delivery.application.dto.UpdateDeliveryResponse;
import com.sparta.msa.delivery.domain.model.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findByUuid(UUID uuid);
}
