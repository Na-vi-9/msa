package com.sparta.msa.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderResponse {
    private UUID orderID;
    private UUID deliveryID;
}
