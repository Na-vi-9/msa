package com.sparta.msa.company.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.company.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.sparta.msa.company.domain.entity.QCompany.company;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final CompanyRepository companyRepository;

    @Override
    public Optional<Company> findByCompanyUUID(UUID companyUUID) {
        JPAQuery<Company> query = jpaQueryFactory
                .select(company)
                .from(company)
                .where(company.isDeleted.eq(false))
                .where(company.companyUUID.eq(companyUUID));
        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Page<Company> searchCompaniesIsDeletedFalse(Predicate predicate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder(predicate);
        builder.and(company.isDeleted.eq(false));
        return companyRepository.findAll(builder, pageable);
    }
}
