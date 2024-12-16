package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.domain.model.QDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, String>,
        QuerydslPredicateExecutor<DeliveryManager> {

    Optional<DeliveryManager> findByUsername(String username);

    Optional<DeliveryManager> findTopByIsDeletedFalseOrderByDeliveryOrderAsc();

    default Integer findLastDeliveryOrder() {
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(deliveryManager.isDeleted.eq(false));
        return (int) count(builder);
    }

    default Page<DeliveryManager> findWithCondition(String condition, String keyword, Pageable pageable) {
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
        BooleanBuilder builder = new BooleanBuilder();

        if (condition != null) {
            builder.and(deliveryManager.type.stringValue().eq(condition));
        }

        if (keyword != null) {
            builder.and(deliveryManager.username.containsIgnoreCase(keyword));
        }

        builder.and(deliveryManager.isDeleted.eq(false));

        return findAll(builder, pageable);
    }
}
