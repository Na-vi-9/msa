package com.sparta.msa.hub.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class HubInfoRequest implements Serializable {
    private UUID departureHubUUID;
    private UUID arrivalHubUUID;
}
