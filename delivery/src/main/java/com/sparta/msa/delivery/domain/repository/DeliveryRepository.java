package com.sparta.msa.delivery.domain.repository;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.domain.model.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findByUuidAndIsDeletedFalse(UUID uuid);

    Page<Delivery> searchDeliveriesIsDeletedFalse(Predicate predicate, Pageable pageable);
}
