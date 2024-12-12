package com.sparta.msa.delivery.application.service;

import com.sparta.msa.delivery.application.dto.deliveryRoute.CreateDeliveryRouteDto;
import com.sparta.msa.delivery.application.dto.hubRoute.HubRouteDto;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import com.sparta.msa.delivery.domain.repository.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryRouteService {
    private final DeliveryRouteRepository deliveryRouteRepository;

    @Transactional
    public void createDeliveryRoute(CreateDeliveryRouteDto request) {
        // TODO
        //  P2P: 출발지와 도착지 허브를 통해 경로 받고 해당 허브 배송담당자 추출
        HubRouteDto hubRouteDto = new HubRouteDto(request.getDepartureHubUUID(), request.getArrivalHubUUID(), 1, 1.0);
        String deliveryManagerUsername = "username";
        Integer sequence = 1;

        deliveryRouteRepository.save(DeliveryRoute.create(request.getDeliveryUUID(), request.getDepartureHubUUID(), request.getArrivalHubUUID(),
                sequence, hubRouteDto.getDistanceKm(), hubRouteDto.getDurationMin(), DeliveryStatus.WAITING_AT_HUB, deliveryManagerUsername));
    }
}