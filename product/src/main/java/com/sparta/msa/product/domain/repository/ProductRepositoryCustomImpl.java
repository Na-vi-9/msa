package com.sparta.msa.product.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.sparta.msa.product.application.dto.ProductResponse;
import com.sparta.msa.product.domain.model.QProduct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<ProductResponse> findProductsWithCondition(String condition, String keyword, Pageable pageable) {
        QProduct product = QProduct.product;

        JPAQuery<ProductResponse> query = new JPAQuery<>(entityManager)
                .select(Projections.constructor(ProductResponse.class,
                        product.uuid,
                        product.name,
                        product.quantity,
                        product.companyUUID,
                        product.hubUUID
                )).from(product);

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
            return QProduct.product.uuid.eq(UUID.fromString(keyword));
        } else if ("name".equalsIgnoreCase(condition)) {
            return QProduct.product.name.containsIgnoreCase(keyword);
        } else if ("companyUUID".equalsIgnoreCase(condition)) {
            return QProduct.product.companyUUID.eq(UUID.fromString(keyword));
        } else if ("hubUUID".equalsIgnoreCase(condition)) {
            return QProduct.product.hubUUID.eq(UUID.fromString(keyword));
        }
        return QProduct.product.uuid.eq(UUID.fromString(keyword))
                .or(QProduct.product.name.containsIgnoreCase(keyword))
                .or(QProduct.product.companyUUID.eq(UUID.fromString(keyword)))
                .or(QProduct.product.hubUUID.eq(UUID.fromString(keyword)));
    }

}
