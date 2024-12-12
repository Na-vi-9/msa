package com.sparta.msa.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID hubUUID;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private Long managerId;

    //BaseEntity와 extends 후 제거 예정
    private Boolean isDeleted = false;

    @PrePersist
    private void prePersistence() {
        hubUUID = UUID.randomUUID();
    }

    public static Hub create(String name, String address, Double latitude, Double longitude, Long managerId) {
        Hub hub = Hub.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .managerId(managerId)
                .build();

        return hub;
    }

    public void update(String name, String address, Double latitude, Double longitude, Long managerId) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managerId = managerId;
    }

    // BaseEntity extends 후 제거 예정
    public void softDeleted() {
        this.isDeleted = true;
    }
}
