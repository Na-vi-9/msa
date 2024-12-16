package com.sparta.msa.company.infrastructure.repository;

import com.sparta.msa.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByCompanyUUID(UUID companyUUID);
}
