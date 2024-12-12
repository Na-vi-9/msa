package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, Long> {
}
