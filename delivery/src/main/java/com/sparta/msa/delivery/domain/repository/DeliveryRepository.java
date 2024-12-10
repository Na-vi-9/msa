package com.sparta.msa.delivery.domain.repository;

import com.sparta.msa.delivery.domain.model.Delivery;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
}
