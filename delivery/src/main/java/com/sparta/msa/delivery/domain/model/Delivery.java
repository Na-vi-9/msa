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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "departure_hub_id", nullable = false)
    private Long departureHubId;

    @Column(name = "arrival_hub_id", nullable = false)
    private Long arrivalHubId;

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

    public static Delivery create(Long orderId,
                                  DeliveryStatus status,
                                  Long departureHubId,
                                  Long arrivalHubId,
                                  String deliveryAddress,
                                  String receiverName,
                                  String receiverSlackId) {
        return Delivery.builder()
                .orderId(orderId)
                .status(status)
                .departureHubId(departureHubId)
                .arrivalHubId(arrivalHubId)
                .deliveryAddress(deliveryAddress)
                .receiverName(receiverName)
                .receiverSlackId(receiverSlackId)
                .build();
    }
}
