package com.sparta.msa.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderResponse {
    private UUID orderUUID;
    private UUID deliveryUUID;
}
