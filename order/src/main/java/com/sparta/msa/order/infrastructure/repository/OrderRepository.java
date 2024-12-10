package com.sparta.msa.order.infrastructure.repository;

import com.sparta.msa.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUuidAndIsDeletedFalse(UUID uuid);

    @Query("SELECT o FROM Order o WHERE o.isDeleted = false AND " +
            "(:condition IS NULL OR o.memo LIKE %:keyword%)")
    Page<Order> findAllWithCondition(@Param("condition") String condition,
                                     @Param("keyword") String keyword,
                                     Pageable pageable);
}
