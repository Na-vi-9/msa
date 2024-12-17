package com.sparta.msa.delivery.infrastructure.client;

import com.sparta.msa.delivery.application.dto.hub.HubDto;
import com.sparta.msa.delivery.application.dto.hubRoute.GetHubRouteRequest;
import com.sparta.msa.delivery.application.dto.hubRoute.HubRouteDto;
import com.sparta.msa.delivery.application.service.HubService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "hub")
public interface HubClient extends HubService {
    @PostMapping("/hubs/route/info")
    CommonResponse<HubRouteDto> getHubRoute(@RequestBody GetHubRouteRequest request);

    @GetMapping("/hubs/{hubUUID}")
    CommonResponse<HubDto> getHub(@PathVariable UUID hubUUID);
}
