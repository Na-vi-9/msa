package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.domain.repository.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {
    private final DeliveryRouteJpaRepository deliveryRouteJpaRepository;

    @Override
    public DeliveryRoute save(DeliveryRoute deliveryRoute) {
        return deliveryRouteJpaRepository.save(deliveryRoute);
    }
}
