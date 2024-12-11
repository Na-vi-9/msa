package com.sparta.msa.delivery.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.sparta.msa.delivery.domain.model.QDelivery.delivery;

@RequiredArgsConstructor
@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findByUuidAndIsDeletedFalse(UUID uuid) {
        JPAQuery<Delivery> query = queryFactory
                .select(delivery)
                .from(delivery)
                .where(delivery.isDeleted.eq(false).and(delivery.uuid.eq(uuid)));

        return Optional.ofNullable(query.fetchOne());
    }
}
