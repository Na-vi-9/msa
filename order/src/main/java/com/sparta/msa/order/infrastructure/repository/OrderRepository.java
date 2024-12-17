package com.sparta.msa.order.infrastructure.repository;

import com.sparta.msa.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Optional<Order> findByUuidAndIsDeletedFalse(UUID uuid);
}
