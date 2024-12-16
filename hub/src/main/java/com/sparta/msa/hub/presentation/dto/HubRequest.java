package com.sparta.msa.hub.presentation.dto;

import com.sparta.msa.hub.application.dto.HubDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class HubRequest implements Serializable {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String managerName;

    public HubDto toDTO() { return HubDto.of(this.name, this.address, this.latitude, this.longitude, this.managerName); }
}
