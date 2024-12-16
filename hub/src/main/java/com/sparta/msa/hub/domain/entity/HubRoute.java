package com.sparta.msa.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub_route")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class HubRoute extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hubRouteUUID;

    private UUID departureHubUUID;

    private UUID arrivalHubUUID;

    private Integer durationMin;

    private Double distanceKm;

    public static HubRoute create(UUID departureHubUUID, UUID arrivalHubUUID, Integer durationMin, Double distance) {
        double distanceKm = distance / 1000.0;

        return HubRoute.builder()
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .durationMin(durationMin)
                .distanceKm(distanceKm)
                .build();
    }

    public void update(int duration, double distance) {
        this.durationMin = duration;
        this.distanceKm = distance / 1000.0;
    }
}
