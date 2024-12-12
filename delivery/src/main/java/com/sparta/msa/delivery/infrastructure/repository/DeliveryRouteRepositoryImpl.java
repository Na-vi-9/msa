package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.domain.repository.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.sparta.msa.delivery.domain.model.QDeliveryRoute.deliveryRoute;

@RequiredArgsConstructor
@Repository
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {
    private final DeliveryRouteJpaRepository deliveryRouteJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public DeliveryRoute save(DeliveryRoute deliveryRoute) {
        return deliveryRouteJpaRepository.save(deliveryRoute);
    }

    @Override
    public Optional<DeliveryRoute> findByUuidAndIsDeletedFalse(UUID uuid) {
        JPAQuery<DeliveryRoute> query = queryFactory
                .select(deliveryRoute)
                .from(deliveryRoute)
                .where(deliveryRoute.isDeleted.eq(false).and(deliveryRoute.uuid.eq(uuid)));

        return Optional.ofNullable(query.fetchOne());
    }
}