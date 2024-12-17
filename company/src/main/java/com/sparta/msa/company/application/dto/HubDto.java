package com.sparta.msa.company.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubDto implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID hubUUID;

    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private String hubManagerName;
}
