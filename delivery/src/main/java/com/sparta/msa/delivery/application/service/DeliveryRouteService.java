package com.sparta.msa.delivery.application.service;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.deliveryRoute.CreateDeliveryRouteDto;
import com.sparta.msa.delivery.application.dto.deliveryRoute.DeliveryRouteResponse;
import com.sparta.msa.delivery.application.dto.deliveryRoute.UpdateDeliveryRouteDto;
import com.sparta.msa.delivery.application.dto.deliveryRoute.UpdateDeliveryRouteResponse;
import com.sparta.msa.delivery.application.dto.hubRoute.HubRouteDto;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import com.sparta.msa.delivery.domain.repository.DeliveryRouteRepository;
import com.sparta.msa.delivery.infrastructure.exception.CustomException;
import com.sparta.msa.delivery.infrastructure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryRouteService {
    private final DeliveryRouteRepository deliveryRouteRepository;
    private final DeliveryManagerService deliveryManagerService;
    private final HubService hubService;

    @Transactional
    public void createDeliveryRoute(CreateDeliveryRouteDto request) {
        HubRouteDto hubRouteDto = hubService.getHubRoute(GetHubRouteRequest.of(request.getDepartureHubUUID(), request.getArrivalHubUUID())).data();

        String deliveryManagerUsername = deliveryManagerService.assignHubDeliveryManager().getUsername();
        Integer sequence = 1;

        deliveryRouteRepository.save(DeliveryRoute.create(request.getDeliveryUUID(), request.getDepartureHubUUID(), request.getArrivalHubUUID(),
                sequence, hubRouteDto.getDistanceKm(), hubRouteDto.getDurationMin(), DeliveryStatus.WAITING_AT_HUB, deliveryManagerUsername)
        );
    }

    @Transactional
    public UpdateDeliveryRouteResponse updateDeliveryRoute(UUID deliveryRouteUUID, UpdateDeliveryRouteDto request) {
        return deliveryRouteRepository.findByUuidAndIsDeletedFalse(deliveryRouteUUID).map(deliveryRoute -> {
            deliveryRoute.update(request.getDeliveryUUID(), request.getDepartureHubUUID(), request.getArrivalHubUUID(),
                    request.getSequence(), request.getEstimatedDistanceKm(), request.getEstimatedTimeMin(),
                    request.getActualDistanceKm(), request.getActualTimeMin(), request.getStatus(), request.getDeliveryManagerUsername()
            );
            return UpdateDeliveryRouteResponse.of(deliveryRoute);
        }).orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND));
    }

    public PagedModel<DeliveryRouteResponse> searchDeliveryRoutesIsDeletedFalse(Predicate predicate, Pageable pageable) {
        Page<DeliveryRoute> deliveryRoutePage = deliveryRouteRepository.searchDeliveryRouteIsDeletedFalse(predicate, pageable);
        return new PagedModel<>(
                new PageImpl<>(
                        deliveryRoutePage.getContent().stream()
                                .map(DeliveryRouteResponse::of)
                                .toList(),
                        deliveryRoutePage.getPageable(),
                        deliveryRoutePage.getTotalElements()
                )
        );
    }

    public DeliveryRouteResponse findDeliveryRouteByUUID(UUID deliveryRouteUUID) {
        return deliveryRouteRepository.findByUuidAndIsDeletedFalse(deliveryRouteUUID)
                .map(DeliveryRouteResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND));
    }
  
    @Transactional
    public void deleteDeliveryRoute(UUID deliveryRouteUUID, String deleteBy) {
        deliveryRouteRepository.findByUuidAndIsDeletedFalse(deliveryRouteUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND))
                .delete(deleteBy);
    }
}
