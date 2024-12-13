package com.sparta.msa.hub.infrastructure.repository;

import com.sparta.msa.hub.domain.entity.Hub;

import java.util.Optional;
import java.util.UUID;

public interface HubQueryRepositoryCustom {

    Optional<Hub> findByUUID(UUID hubUUID);
}
