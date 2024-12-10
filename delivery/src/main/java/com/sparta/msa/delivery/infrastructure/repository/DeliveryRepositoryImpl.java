package com.sparta.msa.delivery.infrastructure.repository;

import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.domain.repository.DeliveryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository deliveryJpaRepository;

    public DeliveryRepositoryImpl(DeliveryJpaRepository deliveryJpaRepository) {
        this.deliveryJpaRepository = deliveryJpaRepository;
    }


    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }
}
