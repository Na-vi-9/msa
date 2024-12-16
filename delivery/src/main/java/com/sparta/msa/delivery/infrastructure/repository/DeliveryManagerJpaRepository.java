package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.domain.model.QDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, String>,
        QuerydslPredicateExecutor<DeliveryManager>,
        QuerydslBinderCustomizer<QDeliveryManager> {

    Optional<DeliveryManager> findByUsername(String username);

    Optional<DeliveryManager> findTopByIsDeletedFalseOrderByDeliveryOrderAsc();

    @Override
    default void customize(QuerydslBindings bindings, QDeliveryManager root) {
        bindings.bind(root.username).first((StringPath path, String value) -> path.containsIgnoreCase(value));

        bindings.excluding(root.isDeleted, root.deliveryOrder);
    }

    default Page<DeliveryManager> findWithKeyword(String keyword, Predicate predicate, Pageable pageable) {
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
        BooleanBuilder builder = new BooleanBuilder(predicate);

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(deliveryManager.username.containsIgnoreCase(keyword));
        }

        return findAll(builder, pageable);
    }


    default Integer findLastDeliveryOrder() {
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(deliveryManager.isDeleted.eq(false));
        return (int) count(builder);
    }
}
