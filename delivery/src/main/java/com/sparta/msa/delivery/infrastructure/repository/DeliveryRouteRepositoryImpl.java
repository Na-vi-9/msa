package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.repository.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {
    private final DeliveryRouteRepository deliveryRouteRepository;
}
