package com.sparta.msa.company.application.service;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.company.application.dto.CompanyDto;
import com.sparta.msa.company.application.dto.CompanyResponse;
import com.sparta.msa.company.application.dto.CreateCompanyResponse;
import com.sparta.msa.company.domain.entity.Company;
import com.sparta.msa.company.domain.exception.CustomException;
import com.sparta.msa.company.domain.exception.ErrorCode;
import com.sparta.msa.company.infrastructure.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CreateCompanyResponse createCompany(CompanyDto request) {
        // TODO: 유저 Role 권한 체크(create)

        Company company = Company.create(
                request.getName(),
                request.getType(),
                request.getHubUUID(),
                request.getAddress()
        );

        companyRepository.save(company);

        return CreateCompanyResponse.from(company.getCompanyUUID());
    }

    @Transactional
    public CompanyResponse updateCompany(UUID companyUUID, CompanyDto request) {
        // TODO: 유저 Role 권한 체크(update)
        Company company = validateCompany(companyUUID);

        company.update(
                request.getName(),
                request.getType(),
                request.getHubUUID(),
                request.getAddress()
        );

        return CompanyResponse.fromCompany(company);
    }

    @Transactional
    public void deleteCompany(UUID companyUUID) {
        // TODO: 유저 Role 권한 체크(delete)
        String deletedManagerName = "master";

        Company company = validateCompany(companyUUID);

        company.markDeleted(deletedManagerName);
    }

    public CompanyResponse getCompany(UUID companyUUID) {
        // TODO: 유저 Role 권한 체크(get 1)
        Company company = validateCompany(companyUUID);

        return CompanyResponse.fromCompany(company);
    }

    public PagedModel<CompanyResponse> findAllCompanies(Predicate predicate, Pageable pageable) {
        // TODO: 유저 Role 권한 체크(get 2)
        Page<Company> companyPage = companyRepository.searchCompaniesIsDeletedFalse(predicate, pageable);

        return new PagedModel<>(
                new PageImpl<>(
                        companyPage.getContent().stream()
                                .map(CompanyResponse::fromCompany)
                                .toList(),
                        companyPage.getPageable(),
                        companyPage.getTotalElements()
                )
        );
    }

    public Company validateCompany(UUID companyUUID) {
        Company company = companyRepository.findByCompanyUUID(companyUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMPANY, companyUUID.toString()));

        if (company.getIsDeleted().equals(true)) {
            throw new CustomException(ErrorCode.DELETED_COMPANY, companyUUID.toString());
        }

        return company;
    }
}
