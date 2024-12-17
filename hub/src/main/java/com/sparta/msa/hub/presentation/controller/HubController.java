package com.sparta.msa.hub.presentation.controller;

import com.sparta.msa.hub.application.dto.CreateHubResponse;
import com.sparta.msa.hub.application.dto.HubResponse;
import com.sparta.msa.hub.application.service.HubService;
import com.sparta.msa.hub.presentation.dto.HubRequest;
import com.sparta.msa.hub.presentation.dto.UserInfoRequest;
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

    @PostMapping
    public CommonResponse<CreateHubResponse> createHub(@RequestHeader("Authorization") String token,
                                                       @RequestHeader("X-Username") String username,
                                                       @RequestHeader("X-Role") String role,
                                                       @RequestBody HubRequest hubRequest) {

        UserInfoRequest userInfo = UserInfoRequest.of(token, username, role);
        return CommonResponse.ofSuccess(hubService.createHub(hubRequest.toDTO(), userInfo));
    }

    @PutMapping("/{hubUUID}")
    public CommonResponse<HubResponse> updateHub(@RequestHeader("Authorization") String token,
                                                 @RequestHeader("X-Username") String username,
                                                 @RequestHeader("X-Role") String role,
                                                 @PathVariable("hubUUID") UUID hubUUID,
                                                 @RequestBody HubRequest hubRequest) {

        UserInfoRequest userInfo = UserInfoRequest.of(token, username, role);
        return CommonResponse.ofSuccess(hubService.updateHub(hubUUID, hubRequest.toDTO(), userInfo));
    }

    @DeleteMapping("/{hubUUID}")
    public CommonResponse<Void> deleteHub(@RequestHeader("Authorization") String token,
                                          @RequestHeader("X-Username") String username,
                                          @RequestHeader("X-Role") String role,
                                          @PathVariable("hubUUID") UUID hubUUID) {

        UserInfoRequest userInfo = UserInfoRequest.of(token, username, role);
        hubService.deleteHub(hubUUID, userInfo);
        return CommonResponse.ofSuccess(null);
    }
}
