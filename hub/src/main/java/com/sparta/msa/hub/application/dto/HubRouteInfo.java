package com.sparta.msa.hub.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.hub.domain.entity.HubRoute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HubRouteInfo implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID departureUUID;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID arrivalUUID;
    private Integer durationMin;
    private Double distanceKm;

    public static HubRouteInfo from(HubRoute hubRoute) {
        return HubRouteInfo.builder()
                .departureUUID(hubRoute.getDepartureHubUUID())
                .arrivalUUID(hubRoute.getArrivalHubUUID())
                .durationMin(hubRoute.getDurationMin())
                .distanceKm(hubRoute.getDistanceKm())
                .build();
    }
}
