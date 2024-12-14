package com.sparta.msa.delivery.infrastructure.repository;

import static com.sparta.msa.delivery.domain.model.QDeliveryManager.deliveryManager;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.domain.model.QDeliveryManager;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, String>,
        QuerydslPredicateExecutor<DeliveryManager>, QuerydslBinderCustomizer<QDeliveryManager> {

    Optional<DeliveryManager> findByUsername(String username);

    Optional<DeliveryManager> findTopByIsDeletedFalseOrderByDeliveryOrderAsc();

    @Query("SELECT MAX(dm.deliveryOrder) FROM DeliveryManager dm WHERE dm.isDeleted = false")
    Optional<Integer> findLastDeliveryOrder();

    @Query("SELECT dm FROM DeliveryManager dm WHERE " +
            "(:condition IS NULL OR dm.type = :condition) AND " +
            "(:keyword IS NULL OR dm.username LIKE %:keyword%)")
    List<DeliveryManager> search(String condition, String keyword);

    @Override
    default void customize(QuerydslBindings bindings, @NotNull QDeliveryManager qDeliveryManager) {
        bindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            BooleanBuilder builder = new BooleanBuilder();
            values.forEach(value -> builder.or(path.containsIgnoreCase(value.trim())));
            return Optional.of(builder);
        });

        bindings.bind(deliveryManager.username).first((path, value) -> path.containsIgnoreCase(value));
        bindings.bind(deliveryManager.hubUUID).first((path, value) -> path.eq(value));
        bindings.bind(deliveryManager.type).first((path, value) -> path.eq(value));
    }
}
