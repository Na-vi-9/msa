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

    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    @Column(name = "receiver_slack_id", nullable = false, length = 20)
    private String receiverSlackId;

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
                                  String receiverName,
                                  String receiverSlackId) {
        return Delivery.builder()
                .orderUUID(orderUUID)
                .status(status)
                .departureHubUUID(departureHubUUID)
                .arrivalHubUUID(arrivalHubUUID)
                .deliveryAddress(deliveryAddress)
                .receiverName(receiverName)
                .receiverSlackId(receiverSlackId)
                .build();
    }

    public void update(UUID orderUUID,
                              DeliveryStatus status,
                              UUID departureHubUUID,
                              UUID arrivalHubUUID,
                              String deliveryAddress,
                              String receiverName,
                              String receiverSlackId) {
        this.orderUUID = orderUUID;
        this.status = status;
        this.departureHubUUID = departureHubUUID;
        this.arrivalHubUUID = arrivalHubUUID;
        this.deliveryAddress = deliveryAddress;
        this.receiverName = receiverName;
        this.receiverSlackId = receiverSlackId;
    }
}
