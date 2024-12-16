package com.sparta.msa.company.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.sparta.msa.company.domain.entity.Company;
import com.sparta.msa.company.domain.entity.QCompany;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.*;

public interface CompanyRepository extends JpaRepository<Company, UUID>,
        CompanyRepositoryCustom,
        QuerydslPredicateExecutor<Company>,
        QuerydslBinderCustomizer<QCompany> {

    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QCompany qCompany) {
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
