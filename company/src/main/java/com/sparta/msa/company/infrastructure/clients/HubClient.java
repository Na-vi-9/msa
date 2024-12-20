package com.sparta.msa.company.infrastructure.clients;

import com.sparta.msa.company.application.dto.HubDto;
import com.sparta.msa.company.presentation.exception.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient {

    @GetMapping("/hubs/{hubUUID}")
    CommonResponse<HubDto> getHub(@PathVariable("hubUUID") UUID hubUUID);
}
