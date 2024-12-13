package com.sparta.msa.hub.infrastructure.repository;

import com.sparta.msa.hub.domain.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface HubRepository extends JpaRepository<Hub, UUID> {

    Optional<Hub> findByHubUUID(UUID hubUUID);
}
