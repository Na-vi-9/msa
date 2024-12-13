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
public class HubRouteResponse implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID hubRouteUUID;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID departureUUID;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID arrivalUUID;
    private Integer durationMin;
    private Double distanceKm;

    public static HubRouteResponse from(HubRoute hubRoute) {
        return HubRouteResponse.builder()
                .hubRouteUUID(hubRoute.getHubRouteUUID())
                .departureUUID(hubRoute.getDepartureHubUUID())
                .arrivalUUID(hubRoute.getArrivalHubUUID())
                .durationMin(hubRoute.getDurationMin())
                .distanceKm(hubRoute.getDistanceKm())
                .build();
    }
}
