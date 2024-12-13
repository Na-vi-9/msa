package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, String> {
    Optional<DeliveryManager> findByUsername(String username);

    Optional<DeliveryManager> findTopByIsDeletedFalseOrderByDeliveryOrderAsc();

    @Query("SELECT MAX(dm.deliveryOrder) FROM DeliveryManager dm WHERE dm.isDeleted = false")
    Integer findLastDeliveryOrder();

    @Query("SELECT dm FROM DeliveryManager dm WHERE " +
            "(:condition IS NULL OR dm.type = :condition) AND " +
            "(:keyword IS NULL OR dm.username LIKE %:keyword%)")
    List<DeliveryManager> search(String condition, String keyword);
}
