package com.sparta.msa.hub.infrastructure.repository;

import com.sparta.msa.hub.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository extends JpaRepository<HubRoute, UUID> {
    Optional<HubRoute> findByDepartureHubUUIDAndArrivalHubUUIDAndIsDeletedIsFalse(UUID departureHubUUID, UUID arrivalHubUUID);

    Optional<HubRoute> findByHubRouteUUIDAndIsDeletedIsFalse(UUID hubRouteUUID);
}
