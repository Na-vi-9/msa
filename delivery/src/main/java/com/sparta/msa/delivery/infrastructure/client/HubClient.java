package com.sparta.msa.delivery.infrastructure.client;

import com.sparta.msa.delivery.application.dto.hubRoute.GetHubRouteRequest;
import com.sparta.msa.delivery.application.dto.hubRoute.HubRouteDto;
import com.sparta.msa.delivery.application.service.HubService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hub")
public interface HubClient extends HubService {
    @PostMapping("/hubs/route/info")
    CommonResponse<HubRouteDto> getHubRoute(@RequestBody GetHubRouteRequest request);
}
