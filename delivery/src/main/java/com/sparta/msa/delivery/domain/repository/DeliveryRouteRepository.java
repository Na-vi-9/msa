package com.sparta.msa.delivery.domain.repository;

import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRouteRepository {
    DeliveryRoute save(DeliveryRoute deliveryRoute);
}
