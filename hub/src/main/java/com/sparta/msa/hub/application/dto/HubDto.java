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
    private String managerName;

    public static HubDto of(String name, String address, Double latitude, Double longitude, String managerName) {
        return HubDto.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .managerName(managerName)
                .build();
    }
}
