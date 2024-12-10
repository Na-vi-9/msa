package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
}
