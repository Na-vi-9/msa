package com.sparta.msa.delivery.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_delivery")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Delivery extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "order_uuid", nullable = false)
    private UUID orderUUID;

    @Column(name = "departure_hub_uuid", nullable = false)
    private UUID departureHubUUID;

    @Column(name = "arrival_hub_uuid", nullable = false)
    private UUID arrivalHubUUID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @Column(name = "recipient_username", nullable = false, length = 10)
    private String recipientUsername;

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

    public static Delivery create(UUID orderUUID,
                                  DeliveryStatus status,
                                  UUID departureHubUUID,
                                  UUID arrivalHubUUID,
                                  String deliveryAddress,
                                  String recipientUsername,
                                  String deliveryManagerUsername) {
        return Delivery.builder()
                .orderUUID(orderUUID)
                .status(status)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .deliveryAddress(deliveryAddress)
                .recipientUsername(recipientUsername)
                .deliveryManagerUsername(deliveryManagerUsername)
                .build();
    }

    public void update(UUID orderUUID,
                       DeliveryStatus status,
                       UUID departureHubUUID,
                       UUID arrivalHubUUID,
                       String deliveryAddress,
                       String recipientUsername,
                       String deliveryManagerUsername) {
        this.orderUUID = orderUUID;
        this.status = status;
        this.departureHubUUID = departureHubUUID;
        this.arrivalHubUUID = arrivalHubUUID;
        this.deliveryAddress = deliveryAddress;
        this.recipientUsername = recipientUsername;
        this.deliveryManagerUsername = deliveryManagerUsername;
    }

    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
