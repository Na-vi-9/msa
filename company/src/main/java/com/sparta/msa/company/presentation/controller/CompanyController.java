package com.sparta.msa.company.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.company.application.dto.CompanyResponse;
import com.sparta.msa.company.application.dto.CreateCompanyResponse;
import com.sparta.msa.company.application.service.CompanyService;
import com.sparta.msa.company.domain.entity.Company;
import com.sparta.msa.company.presentation.dto.CompanyRequest;
import com.sparta.msa.company.presentation.dto.UserInfoRequest;
import com.sparta.msa.company.presentation.exception.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public CommonResponse<CreateCompanyResponse> createCompany(@RequestHeader("Authorization") String token,
                                                               @RequestHeader("X-Username") String username,
                                                               @RequestHeader("X-Role") String role,
                                                               @RequestBody CompanyRequest companyRequest) {

        UserInfoRequest userInfoRequest = UserInfoRequest.of(token, username, role);
        return CommonResponse.ofSuccess(companyService.createCompany(companyRequest.toDto(), userInfoRequest));
    }

    @PutMapping("/{companyUUID}")
    public CommonResponse<CompanyResponse> updateCompany(@RequestHeader("Authorization") String token,
                                                         @RequestHeader("X-Username") String username,
                                                         @RequestHeader("X-Role") String role,
                                                         @PathVariable("companyUUID") UUID companyUUID,
                                                         @RequestBody CompanyRequest companyRequest) {

        log.info("username: {}", username);

        UserInfoRequest userInfoRequest = UserInfoRequest.of(token, username, role);
        return CommonResponse.ofSuccess(companyService.updateCompany(companyUUID, companyRequest.toDto(), userInfoRequest));
    }

    @DeleteMapping("/{companyUUID}")
    public CommonResponse<Void> deleteCompany(@RequestHeader("Authorization") String token,
                                              @RequestHeader("X-Username") String username,
                                              @RequestHeader("X-Role") String role,
                                              @PathVariable("companyUUID") UUID companyUUID) {

        UserInfoRequest userInfoRequest = UserInfoRequest.of(token, username, role);
        companyService.deleteCompany(companyUUID, userInfoRequest);
        return CommonResponse.ofSuccess(null);
    }

    @GetMapping("/{companyUUID}")
    public CommonResponse<CompanyResponse> getCompany(@PathVariable("companyUUID") UUID companyUUID) {
        return CommonResponse.ofSuccess(companyService.getCompany(companyUUID));
    }

    @GetMapping
    public CommonResponse<PagedModel<CompanyResponse>> getAllCompanies(@QuerydslPredicate(root = Company.class) Predicate predicate,
                                                                       @PageableDefault Pageable pageable) {
        Pageable adjustedPageable = adjustPageSize(pageable, List.of(10, 30, 50), 10);

        return CommonResponse.ofSuccess(companyService.findAllCompanies(predicate, adjustedPageable));
    }

    private Pageable adjustPageSize(Pageable pageable, List<Integer> allowSizes, int defaultSize) {
        if(!allowSizes.contains(pageable.getPageSize())) {
            return PageRequest.of(pageable.getPageNumber(), defaultSize, pageable.getSort());
        }
        return pageable;
    }
}
