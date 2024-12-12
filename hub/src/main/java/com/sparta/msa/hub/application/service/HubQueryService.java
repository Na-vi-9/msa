package com.sparta.msa.hub.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.sparta.msa.hub.application.dto.GetHubsResponse;
import com.sparta.msa.hub.application.dto.HubResponse;
import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.domain.entity.QHub;
import com.sparta.msa.hub.domain.exception.CustomException;
import com.sparta.msa.hub.domain.exception.ErrorCode;
import com.sparta.msa.hub.infrastructure.repository.HubQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class HubQueryService {

    // Redis를 사용한 캐시는 추후 추가
    private final HubQueryRepository hubQueryRepository;

    public HubQueryService(HubQueryRepository hubQueryRepository) {
        this.hubQueryRepository = hubQueryRepository;
    }

    @Cacheable(cacheNames = "hubCache", key = "#hubUUID")
    public HubResponse getHub(UUID hubUUID) {
        Hub hub = hubQueryRepository.findByUUID(hubUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, hubUUID.toString()));


        return HubResponse.fromHub(hub);
    }

    @Cacheable(cacheNames = "hubsCache", key = "'allHubs_' + #predicate.hashCode() + '_' + #pageable.pageNumber")
    public GetHubsResponse findAllHubs(Predicate predicate, Pageable pageable) {
        BooleanBuilder defaultCondition = new BooleanBuilder(QHub.hub.isDeleted.eq(false));
        defaultCondition.and(predicate);
        System.out.println(defaultCondition);
        Page<Hub> hubEntityPage = hubQueryRepository.findAll(defaultCondition, pageable);
        return GetHubsResponse.of(hubEntityPage);
//        Page<Hub> hubEntityPage = hubQueryRepository.findAll(predicate, pageable);
//        return GetHubsResponse.of(hubEntityPage);
    }
}
