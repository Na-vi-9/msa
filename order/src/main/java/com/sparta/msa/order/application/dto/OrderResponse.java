package com.sparta.msa.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private UUID orderUUID;
    private UUID deliveryUUID;
}
