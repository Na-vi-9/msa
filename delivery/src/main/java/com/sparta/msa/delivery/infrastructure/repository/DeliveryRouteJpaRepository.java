package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.domain.model.QDeliveryRoute;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.*;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, UUID>,
        QuerydslPredicateExecutor<DeliveryRoute>,
        QuerydslBinderCustomizer<QDeliveryRoute> {
    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QDeliveryRoute qDelivery) {
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
            if (valueList.isEmpty()) {
                return Optional.empty();
            }
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String s : valueList) {
                booleanBuilder.or(path.containsIgnoreCase(s));
            }
            return Optional.of(booleanBuilder);
        });
    }
}
