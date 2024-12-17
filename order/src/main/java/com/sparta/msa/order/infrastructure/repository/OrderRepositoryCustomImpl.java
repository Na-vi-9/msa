package com.sparta.msa.order.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.sparta.msa.order.application.dto.OrderListResponse;
import com.sparta.msa.order.domain.model.QOrder;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<OrderListResponse> findProductsWithCondition(String condition, String keyword, Pageable pageable) {
        QOrder order = QOrder.order;

        JPAQuery<OrderListResponse> query = new JPAQuery<>(entityManager)
                .select(Projections.constructor(OrderListResponse.class,
                        order.uuid,
                        order.supplierCompanyUUID,
                        order.receiverCompanyUUID,
                        order.productUUID,
                        order.quantity,
                        order.memo
                ))
                .from(order);

        if (condition != null && keyword != null) {
            query.where(buildSearchCondition(condition, keyword));
        }
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        long total = query.fetchCount();
        return new PageImpl<>(query.fetch(), pageable, total);
    }

    private BooleanExpression buildSearchCondition(String condition, String keyword) {
        if ("uuid".equalsIgnoreCase(condition)) {
            return QOrder.order.uuid.eq(UUID.fromString(keyword));
        } else if ("supplierCompanyUUID".equalsIgnoreCase(condition)) {
            return QOrder.order.supplierCompanyUUID.eq(UUID.fromString(keyword));
        } else if ("receiverCompanyUUID".equalsIgnoreCase(condition)) {
            return QOrder.order.receiverCompanyUUID.eq(UUID.fromString(keyword));
        } else if ("productUUID".equalsIgnoreCase(condition)) {
            return QOrder.order.productUUID.eq(UUID.fromString(keyword));
        } else if ("memo".equalsIgnoreCase(condition)) {
            return QOrder.order.memo.containsIgnoreCase(keyword);
        }
        return QOrder.order.uuid.eq(UUID.fromString(keyword))
                .or(QOrder.order.supplierCompanyUUID.eq(UUID.fromString(keyword)))
                .or(QOrder.order.receiverCompanyUUID.eq(UUID.fromString(keyword)))
                .or(QOrder.order.productUUID.eq(UUID.fromString(keyword)))
                .or(QOrder.order.memo.containsIgnoreCase(keyword));
    }
}
