package com.sparta.msa.hub.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.hub.application.dto.GetHubsResponse;
import com.sparta.msa.hub.application.dto.HubResponse;
import com.sparta.msa.hub.application.service.HubQueryService;
import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.presentation.exception.CommonResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/hubs")
public class HubQueryController {

    private final HubQueryService hubQueryService;

    public HubQueryController(HubQueryService hubQueryService) {
        this.hubQueryService = hubQueryService;
    }

    @GetMapping("/{hubUUID}")
    public CommonResponse<HubResponse> getHub(@PathVariable("hubUUID") UUID hubUUID) {
        return CommonResponse.ofSuccess(hubQueryService.getHub(hubUUID));
    }

    @GetMapping
    public CommonResponse<GetHubsResponse> findAllHubs(
            @QuerydslPredicate(root = Hub.class) Predicate predicate,
            @PageableDefault Pageable pageable
    ) {

        return CommonResponse.ofSuccess(hubQueryService.findAllHubs(predicate, pageable));
    }
}
