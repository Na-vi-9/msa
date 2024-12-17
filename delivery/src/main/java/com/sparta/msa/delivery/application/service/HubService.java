package com.sparta.msa.delivery.application.service;

import com.sparta.msa.delivery.application.dto.hub.HubDto;
import com.sparta.msa.delivery.application.dto.hubRoute.GetHubRouteRequest;
import com.sparta.msa.delivery.application.dto.hubRoute.HubRouteDto;
import com.sparta.msa.delivery.common.dto.CommonResponse;

import java.util.UUID;

public interface HubService {
    CommonResponse<HubRouteDto> getHubRoute(GetHubRouteRequest request);
    CommonResponse<HubDto> getHub(UUID hubUUID);

}
