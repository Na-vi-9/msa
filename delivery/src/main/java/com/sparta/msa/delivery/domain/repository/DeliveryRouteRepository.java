package com.sparta.msa.delivery.domain.repository;

import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRouteRepository {
    DeliveryRoute save(DeliveryRoute deliveryRoute);

    Optional<DeliveryRoute> findByUuidAndIsDeletedFalse(UUID uuid);
}
