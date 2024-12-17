package com.sparta.msa.order.infrastructure.client;

import com.sparta.msa.order.exception.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "hub", url = "http://localhost:19094")
public interface HubRouteFeignClient {

    @PostMapping("/hubs/route/info")
    CommonResponse<HubRouteInfoResponse> getHubRouteId(@RequestBody HubRouteInfoRequest request);

    @GetMapping("/hubs/{hubUUID}")
    CommonResponse<HubInfoResponse> getHub(@PathVariable("hubUUID") UUID hubUUID);
}
