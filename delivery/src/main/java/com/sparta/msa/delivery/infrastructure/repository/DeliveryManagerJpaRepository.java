package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import com.sparta.msa.delivery.domain.model.DeliveryManagerType;
import com.sparta.msa.delivery.domain.model.QDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, String>,
        QuerydslPredicateExecutor<DeliveryManager>,
        QuerydslBinderCustomizer<QDeliveryManager> {

    Optional<DeliveryManager> findByUsername(String username);

    Optional<DeliveryManager> findTopByIsDeletedFalseOrderByDeliveryOrderAsc();

    // 허브 배송 담당자 배정용
    Optional<DeliveryManager> findTopByTypeAndIsDeletedFalseOrderByDeliveryOrderAsc(DeliveryManagerType type);

    // 특정 허브의 업체 배송 담당자 배정용
    Optional<DeliveryManager> findTopByTypeAndHubUUIDAndIsDeletedFalseOrderByDeliveryOrderAsc(
            DeliveryManagerType type, UUID hubUUID);

    // 허브 배송 담당자의 마지막 배정 순서 조회
    @Query("SELECT MAX(dm.deliveryOrder) FROM DeliveryManager dm WHERE dm.type = :type AND dm.isDeleted = false")
    Integer findLastDeliveryOrderByType(@Param("type") DeliveryManagerType type);

    // 특정 허브의 업체 배송 담당자의 마지막 배정 순서 조회
    @Query("SELECT MAX(dm.deliveryOrder) FROM DeliveryManager dm " +
            "WHERE dm.type = :type AND dm.hubUUID = :hubUUID AND dm.isDeleted = false")
    Integer findLastDeliveryOrderByTypeAndHubUUID(@Param("type") DeliveryManagerType type,
                                                  @Param("hubUUID") UUID hubUUID);

    @Query("SELECT MAX(d.deliveryOrder) FROM DeliveryManager d WHERE d.hubUUID = :hubUUID AND d.isDeleted = false")
    Integer findLastDeliveryOrderByHubUUID(UUID hubUUID);

    default Integer findLastDeliveryOrder() {
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(deliveryManager.isDeleted.eq(false));
        return (int) count(builder);
    }

    @Override
    default void customize(QuerydslBindings bindings, QDeliveryManager root) {
        bindings.bind(root.username).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.isDeleted, root.deliveryOrder);
    }

    default Page<DeliveryManager> findWithPredicate(Predicate predicate, Pageable pageable) {
        QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
        BooleanBuilder builder = new BooleanBuilder(predicate);
        builder.and(deliveryManager.isDeleted.eq(false));
        return findAll(builder, pageable);
    }
}
