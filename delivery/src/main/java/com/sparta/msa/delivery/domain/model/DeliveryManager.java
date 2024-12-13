package com.sparta.msa.delivery.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DeliveryManager extends BaseEntity {

    @Id
    @Column(length = 10, nullable = false)
    private String username;

    @Column(name = "hub_uuid", nullable = false)
    private UUID hubUUID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryManagerType type;

    @Column(name = "delivery_order", nullable = false)
    private int deliveryOrder;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 10)
    private String deletedBy;

    public static DeliveryManager create(UUID hubUUID,
                                         DeliveryManagerType type,
                                         String username,
                                         int deliveryOrder) {
        return DeliveryManager.builder()
                .hubUUID(hubUUID)
                .type(type)
                .username(username)
                .deliveryOrder(deliveryOrder)
                .build();
    }

    public void update(UUID hubUUID,
                       DeliveryManagerType type,
                       int deliveryOrder) {
        this.hubUUID = hubUUID;
        this.type = type;
        this.deliveryOrder = deliveryOrder;
    }

    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public void updateDeliveryOrder(int newOrder) {
        this.deliveryOrder = newOrder;
    }
}
