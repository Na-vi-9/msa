package com.sparta.msa.delivery.application.service;

import com.sparta.msa.delivery.application.dto.hubRoute.GetHubRouteRequest;
import com.sparta.msa.delivery.application.dto.hubRoute.HubRouteDto;
import com.sparta.msa.delivery.common.dto.CommonResponse;

public interface HubService {
    CommonResponse<HubRouteDto> getHubRoute(GetHubRouteRequest request);
}
