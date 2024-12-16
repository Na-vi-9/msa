package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.domain.model.QDeliveryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerRepositoryImpl {

    public Predicate buildSearchPredicate(String condition, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;

        if (condition != null) {
            builder.and(deliveryManager.type.stringValue().eq(condition));
        }

        if (keyword != null) {
            builder.and(deliveryManager.username.containsIgnoreCase(keyword));
        }

        builder.and(deliveryManager.isDeleted.eq(false));
        return builder;
    }
}
