package com.sparta.msa.hub.presentation.controller;

import com.sparta.msa.hub.application.dto.HubRouteInfo;
import com.sparta.msa.hub.application.dto.HubRouteResponse;
import com.sparta.msa.hub.application.service.HubRouteService;
import com.sparta.msa.hub.presentation.dto.HubInfoRequest;
import com.sparta.msa.hub.presentation.exception.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hubs/route")
public class HubRouteController {

    private final HubRouteService hubRouteService;

    /* 허브 전체 경로 저장은 API 무료에서 제한이 있기 때문에 미사용 처리*/
    @PostMapping("/all")
    public CommonResponse<String> generateRouteInfo() {
        hubRouteService.calculateAndSaveAllHubRoutes();
        return CommonResponse.ofSuccess("모든 허브 간 이동 경로가 저장되었습니다.");
    }

    @PostMapping
    public CommonResponse<HubRouteResponse> generateRouteInfo(@RequestBody HubInfoRequest hubInfoRequest) {
        HubRouteResponse response = HubRouteResponse
                .from(hubRouteService.calculateAndSaveHubRoute(
                        hubInfoRequest.getDepartureHubUUID(),
                        hubInfoRequest.getArrivalHubUUID()));
        return CommonResponse.ofSuccess(response);
    }

    @GetMapping("/{hubRouteUUID}")
    public CommonResponse<HubRouteResponse> getHubRoute(@PathVariable UUID hubRouteUUID) {
        return CommonResponse.ofSuccess(hubRouteService.getHubRoute(hubRouteUUID));
    }

    @GetMapping
    public CommonResponse<List<HubRouteResponse>> getAllHubRoutes() {
        return CommonResponse.ofSuccess(hubRouteService.getAllHubRoute());
    }

    @PutMapping("/{hubRouteUUID}")
    public CommonResponse<HubRouteResponse> updateHubRoute(@PathVariable UUID hubRouteUUID) {
        return CommonResponse.ofSuccess(hubRouteService.updateHubRoute(hubRouteUUID));
    }

    @DeleteMapping("/{hubRouteUUID}")
    public CommonResponse<String> deleteHubRoute(@PathVariable UUID hubRouteUUID) {
        hubRouteService.deleteHubRoute(hubRouteUUID);
        return CommonResponse.ofSuccess("허브 경로를 삭제했습니다.");
    }

    @PostMapping("/info")
    public CommonResponse<HubRouteInfo> getRouteByDepartureAndArrival(
            @RequestBody HubInfoRequest hubInfoRequest) {
        return CommonResponse.ofSuccess(hubRouteService.getRouteByDepartureAndArrival(hubInfoRequest));
    }
}
