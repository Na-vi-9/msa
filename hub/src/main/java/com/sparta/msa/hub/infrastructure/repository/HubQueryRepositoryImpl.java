package com.sparta.msa.hub.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.hub.domain.entity.Hub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.sparta.msa.hub.domain.entity.QHub.hub;

@Repository
@RequiredArgsConstructor
public class HubQueryRepositoryImpl implements HubQueryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Hub> findByUUID(UUID hubUUID) {
        JPAQuery<Hub> query = jpaQueryFactory
                .select(hub)
                .from(hub)
                .where(hub.isDeleted.eq(false))
                .where(hub.hubUUID.eq(hubUUID));
        return Optional.ofNullable(query.fetchOne());
    }
}
