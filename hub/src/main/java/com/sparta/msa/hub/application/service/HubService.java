package com.sparta.msa.hub.application.service;

import com.sparta.msa.hub.application.dto.CreateHubResponse;
import com.sparta.msa.hub.application.dto.HubDto;
import com.sparta.msa.hub.application.dto.HubResponse;
import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.domain.exception.CustomException;
import com.sparta.msa.hub.domain.exception.ErrorCode;
import com.sparta.msa.hub.infrastructure.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    @CacheEvict(cacheNames = "hubsCache", allEntries = true)
    public CreateHubResponse createHub(HubDto request) {
        // 유저 검증 메서드 호출 필요(테스트를 위해 임시 값 사용)
        String validateManagerId = "master";

        Hub hub = Hub.create(
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude(),
                validateManagerId
        );

        hubRepository.save(hub);

        return CreateHubResponse.toResponse(hub.getHubUUID());
    }

    @Transactional
    @CachePut(cacheNames = "hubCache", key = "#hubUUID")
    public HubResponse updateHub(UUID hubUUID, HubDto request) {
        // 유저 검증 메서드 호출 필요
        String validateManagerId = "master";

        Hub hub = hubRepository.findByHubUUID(hubUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, hubUUID.toString()));

        hub.update(
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude(),
                validateManagerId);

        return HubResponse.fromHub(hub);
    }

    @Transactional
    @CacheEvict(cacheNames = "hubCache", key = "#hubUUID")
    public void deleteHub(UUID hubUUID) {
        // 삭제자 정보 - 임시 데이터 사용 추후 User 서비스 결합 후 삭제하는 관리자 id 추가
        String deletedManagerId = "master";

        // Soft Delete 방식 1
        Hub hub = hubRepository.findByHubUUID(hubUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, hubUUID.toString()));

        hub.markDeleted(deletedManagerId);
    }
}
