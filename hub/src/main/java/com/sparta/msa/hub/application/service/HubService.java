package com.sparta.msa.hub.application.service;

import com.sparta.msa.hub.application.dto.CreateHubResponse;
import com.sparta.msa.hub.application.dto.HubDto;
import com.sparta.msa.hub.application.dto.HubResponse;
import com.sparta.msa.hub.application.dto.UserDto;
import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.domain.enums.Role;
import com.sparta.msa.hub.domain.exception.CustomException;
import com.sparta.msa.hub.domain.exception.ErrorCode;
import com.sparta.msa.hub.infrastructure.clients.UserClient;
import com.sparta.msa.hub.infrastructure.repository.HubRepository;
import com.sparta.msa.hub.presentation.dto.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.security.AuthorizationAuditListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final UserClient userClient;

    @Transactional
    @CacheEvict(cacheNames = "hubsCache", allEntries = true)
    public CreateHubResponse createHub(HubDto request, UserInfoRequest userInfo) {
        UserDto validateUserInfo = validateUsername(userInfo);

        if (validateUserInfo.getRole().equals(Role.MASTER)) {
            Hub hub = Hub.create(
                    request.getName(),
                    request.getAddress(),
                    request.getLatitude(),
                    request.getLongitude(),
                    validateUserInfo.getUsername()
                    );

            hubRepository.save(hub);

            return CreateHubResponse.toResponse(hub.getHubUUID());
        }

        throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    @Transactional
    @CachePut(cacheNames = "hubCache", key = "#hubUUID")
    public HubResponse updateHub(UUID hubUUID, HubDto request, UserInfoRequest userInfo) {
        UserDto validateUserInfo = validateUsername(userInfo);

        if (validateUserInfo.getRole().equals(Role.MASTER)) {
            Hub hub = validateHub(hubUUID);

            hub.update(
                    request.getName(),
                    request.getAddress(),
                    request.getLatitude(),
                    request.getLongitude(),
                    validateUserInfo.getUsername()
            );

            return HubResponse.fromHub(hub);
        }

        throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    @Transactional
    @CacheEvict(cacheNames = "hubCache", key = "#hubUUID")
    public void deleteHub(UUID hubUUID, UserInfoRequest userInfo) {
        UserDto validateUserInfo = validateUsername(userInfo);

        if (validateUserInfo.getRole().equals(Role.MASTER)) {
            Hub hub = validateHub(hubUUID);

            hub.markDeleted(validateUserInfo.getUsername());
        }

        throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    public Hub validateHub(UUID hubUUID) {
        Hub hub = hubRepository.findByHubUUID(hubUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, hubUUID.toString()));

        if (hub.getIsDeleted().equals(true)) {
            throw new CustomException(ErrorCode.DELETED_HUB, hubUUID.toString());
        }

        return hub;
    }

    private UserDto validateUsername(UserInfoRequest request) {
        return userClient.getUserInfo(request.getToken(), request.getUsername()).data();
    }
}
