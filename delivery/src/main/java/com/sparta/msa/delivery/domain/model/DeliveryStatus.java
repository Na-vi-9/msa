package com.sparta.msa.delivery.domain.model;

public enum DeliveryStatus {
    WAITING_AT_HUB,
    MOVING_BETWEEN_HUBS,
    ARRIVED_AT_DESTINATION_HUB,
    DELIVERY_IN_PROGRESS,
    DELIVERY_COMPLETED
}
