package com.sparta.msa.company.domain.repository;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.company.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findByCompanyUUID(UUID companyUUID);
    Page<Company> searchCompaniesIsDeletedFalse(Predicate predicate, Pageable pageable);
}
