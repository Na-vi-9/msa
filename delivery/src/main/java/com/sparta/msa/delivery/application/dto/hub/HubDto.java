package com.sparta.msa.delivery.application.dto.hub;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class HubDto {
    private UUID hubUUID;
    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private String hubManagerName;
}
