package com.sparta.msa.hub.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.domain.entity.QHub;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.*;

public interface HubQueryRepository extends JpaRepository<Hub, UUID>,
        HubQueryRepositoryCustom,
        QuerydslPredicateExecutor<Hub>,
        QuerydslBinderCustomizer<QHub> {

    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QHub qHub) {
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
