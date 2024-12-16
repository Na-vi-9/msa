package com.sparta.msa.company.infrastructure.repository;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.company.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepositoryCustom {

    Optional<Company> findByCompanyUUID(UUID companyUUID);

    Page<Company> searchCompaniesIsDeletedFalse(Predicate predicate, Pageable pageable);
}
