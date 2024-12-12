package com.sparta.msa.delivery.domain.repository;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRouteRepository {
    DeliveryRoute save(DeliveryRoute deliveryRoute);
    Page<DeliveryRoute> searchDeliveryRouteIsDeletedFalse(Predicate predicate, Pageable pageable);

    Optional<DeliveryRoute> findByUuidAndIsDeletedFalse(UUID uuid);
}
