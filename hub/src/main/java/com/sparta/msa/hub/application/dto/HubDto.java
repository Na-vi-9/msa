package com.sparta.msa.hub.application.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HubDto implements Serializable {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long managerId;

    public static HubDto create(String name, String address, Double latitude, Double longitude, Long managerId) {
        return HubDto.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .managerId(managerId)
                .build();
    }
}
