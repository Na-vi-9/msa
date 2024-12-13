package com.sparta.msa.hub.presentation.controller;

import com.sparta.msa.hub.application.dto.CreateHubResponse;
import com.sparta.msa.hub.application.dto.HubResponse;
import com.sparta.msa.hub.application.service.HubService;
import com.sparta.msa.hub.presentation.dto.HubRequest;
import com.sparta.msa.hub.presentation.exception.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/hubs")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    // 추후 @PreAuth 권한 검증 추가 - 현재 테스트를 위해 미사용(Security 미 추가)
    @PostMapping
    public CommonResponse<CreateHubResponse> createHub(@RequestBody HubRequest hubRequest) {
        // 주문 생성 시 발생하는 예외 처리 추가
        return CommonResponse.ofSuccess(hubService.createHub(hubRequest.toDTO()));
    }

    @PutMapping("/{hubUUID}")
    public CommonResponse<HubResponse> updateHub(@PathVariable("hubUUID") UUID hubUUID,
                                                 @RequestBody HubRequest hubRequest) {
        return CommonResponse.ofSuccess(hubService.updateHub(hubUUID, hubRequest.toDTO()));
    }

    @DeleteMapping("/{hubUUID}")
    public CommonResponse<Void> deleteHub(@PathVariable("hubUUID") UUID hubUUID) {
        hubService.deleteHub(hubUUID);
        return CommonResponse.ofSuccess(null);
    }
}
