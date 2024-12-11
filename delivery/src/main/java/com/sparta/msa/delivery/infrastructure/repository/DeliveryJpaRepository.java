package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByUuid(UUID uuid);
}
