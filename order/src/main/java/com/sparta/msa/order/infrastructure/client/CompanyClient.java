package com.sparta.msa.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "company", url = "http://localhost:19093")
public interface CompanyClient {

    @GetMapping("/companies/{companyUUID}/exists")
    boolean checkCompanyExists(@PathVariable UUID companyUUID);
}
