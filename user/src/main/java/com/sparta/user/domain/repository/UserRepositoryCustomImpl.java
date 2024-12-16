package com.sparta.user.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.sparta.user.domain.model.QUser;
import com.sparta.user.presentation.response.UserSearchResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<UserSearchResponseDto> findUsersWithConditions(String condition, String keyword, Pageable pageable) {

        QUser user = QUser.user;

        JPAQuery<UserSearchResponseDto> query = new JPAQuery<>(entityManager)
                .select(Projections.constructor(UserSearchResponseDto.class,
                        user.id,
                        user.username,
                        user.password,
                        user.name,
                        user.email,
                        user.role,
                        user.createdBy,
                        user.createdAt,
                        user.updatedBy,
                        user.updatedAt,
                        user.isDeleted,
                        user.deletedBy,
                        user.deletedAt
                ))
                .from(user);

        // 검색 조건이 있을 경우에만 필터 추가
        if (condition != null && keyword != null) {
            query.where(buildSearchCondition(condition, keyword));
        }

        // 페이징
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 결과 반환
        long total = query.fetchCount();  // 전체 갯수
        return new PageImpl<>(query.fetch(), pageable, total);
    }

    // 동적 검색 조건을 만드는 메서드
    private BooleanExpression buildSearchCondition(String condition, String keyword) {
        if ("username".equalsIgnoreCase(condition)) {
            return QUser.user.username.containsIgnoreCase(keyword);
        } else if ("email".equalsIgnoreCase(condition)) {
            return QUser.user.email.containsIgnoreCase(keyword);
        } else if ("name".equalsIgnoreCase(condition)) {
            return QUser.user.name.containsIgnoreCase(keyword);
        }
        // 기본적으로 모든 조건에서 검색
        return QUser.user.username.containsIgnoreCase(keyword)
                .or(QUser.user.email.containsIgnoreCase(keyword))
                .or(QUser.user.name.containsIgnoreCase(keyword));
    }
}
