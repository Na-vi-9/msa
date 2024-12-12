package com.sparta.msa.delivery.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_delivery_route")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DeliveryRoute extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "delivery_uuid", nullable = false)
    private UUID deliveryUUID;

    @Column(name = "departure_hub_uuid", nullable = false)
    private UUID departureHubUUID;

    @Column(name = "arrival_hub_uuid", nullable = false)
    private UUID arrivalHubUUID;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "estimated_distance_km", nullable = false)
    private Double estimatedDistanceKm;

    @Column(name = "estimated_time_min", nullable = false)
    private Integer estimatedTimeMin;

    @Column(name = "actual_distance_km")
    private Double actualDistanceKm;

    @Column(name = "actual_time_min")
    private Integer actualTimeMin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "delivery_manager_username", nullable = false, length = 10)
    private String deliveryManagerUsername;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 10)
    private String deletedBy;

    public static DeliveryRoute create(UUID deliveryUUID,
                                       UUID departureHubUUID,
                                       UUID arrivalHubUUID,
                                       Integer sequence,
                                       Double estimatedDistanceKm,
                                       Integer estimatedTimeMin,
                                       DeliveryStatus status,
                                       String deliveryManagerUsername) {
        return DeliveryRoute.builder()
                .deliveryUUID(deliveryUUID)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .sequence(sequence)
                .estimatedDistanceKm(estimatedDistanceKm)
                .estimatedTimeMin(estimatedTimeMin)
                .status(status)
                .deliveryManagerUsername(deliveryManagerUsername)
                .build();
    }
}
