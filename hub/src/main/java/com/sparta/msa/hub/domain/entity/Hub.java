package com.sparta.msa.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_hub")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Hub extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hubUUID;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private String managerName;

    public static Hub create(String name, String address, Double latitude, Double longitude, String managerName) {

        return Hub.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .managerName(managerName)
                .build();
    }

    public void update(String name, String address, Double latitude, Double longitude, String managerName) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managerName = managerName;
    }
}
