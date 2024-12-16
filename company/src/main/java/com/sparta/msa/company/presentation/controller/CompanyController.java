package com.sparta.msa.company.presentation.controller;

import com.sparta.msa.company.application.dto.CompanyResponse;
import com.sparta.msa.company.application.dto.CreateCompanyResponse;
import com.sparta.msa.company.application.service.CompanyService;
import com.sparta.msa.company.presentation.dto.CompanyRequest;
import com.sparta.msa.company.presentation.exception.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public CommonResponse<CreateCompanyResponse> createCompany(@RequestBody CompanyRequest companyRequest) {

        return CommonResponse.ofSuccess(companyService.createCompany(companyRequest.toDto()));
    }

    @PutMapping("/{companyUUID}")
    public CommonResponse<CompanyResponse> updateCompany(@PathVariable("companyUUID") UUID companyUUID,
                                                         @RequestBody CompanyRequest companyRequest) {
        return CommonResponse.ofSuccess(companyService.updateCompany(companyUUID, companyRequest.toDto()));
    }

    @DeleteMapping("/{companyUUID}")
    public CommonResponse<Void> deleteCompany(@PathVariable("companyUUID") UUID companyUUID) {
        companyService.deleteCompany(companyUUID);
        return CommonResponse.ofSuccess(null);
    }

    @GetMapping("/{companyUUID}")
    public CommonResponse<CompanyResponse> getCompany(@PathVariable("companyUUID") UUID companyUUID) {
        return CommonResponse.ofSuccess(companyService.getCompany(companyUUID));
    }

    @GetMapping
    public CommonResponse<Page<CompanyResponse>> getAllCompanies(@PageableDefault Pageable pageable) {

        return CommonResponse.ofSuccess(companyService.findAllCompanies(pageable));
    }
}
