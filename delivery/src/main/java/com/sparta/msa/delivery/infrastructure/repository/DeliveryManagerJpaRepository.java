package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, String> {

    @Query("SELECT MAX(dm.deliveryOrder) FROM DeliveryManager dm WHERE dm.isDeleted = false")
    Integer findLastDeliveryOrder();
}
