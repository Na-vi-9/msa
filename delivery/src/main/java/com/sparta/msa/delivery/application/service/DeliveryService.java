package com.sparta.msa.delivery.application.service;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.delivery.*;
import com.sparta.msa.delivery.application.dto.deliveryRoute.CreateDeliveryRouteDto;
import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.domain.model.QDeliveryRoute;
import com.sparta.msa.delivery.domain.repository.DeliveryRepository;
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

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteService deliveryRouteService;
    private final DeliveryManagerService deliveryManagerService;

    @Transactional
    public CreateDeliveryResponse createDelivery(CreateDeliveryDto request) {
        String companyDeliveryManagerUsername = deliveryManagerService.assignCompanyDeliveryManager(request.getDepartureHubUUID()).getUsername();

        Delivery delivery = deliveryRepository.save(
                Delivery.create(request.getOrderUUID(), request.getStatus(), request.getDepartureHubUUID(),
                        request.getArrivalHubUUID(), request.getDeliveryAddress(),
                        request.getRecipientUsername(), companyDeliveryManagerUsername)
        );
        deliveryRouteService.createDeliveryRoute(
                CreateDeliveryRouteDto.of(delivery.getUuid(), request.getDepartureHubUUID(), request.getArrivalHubUUID())
        );

        return CreateDeliveryResponse.of(delivery);

    }

    @Transactional
    public UpdateDeliveryResponse updateDelivery(UUID deliveryUUID, UpdateDeliveryDto request) {

        return deliveryRepository.findByUuidAndIsDeletedFalse(deliveryUUID).map(delivery -> {
            delivery.update(request.getOrderUUID(), request.getStatus(), request.getDepartureHubUUID(), request.getArrivalHubUUID(), request.getDeliveryAddress(), request.getRecipientUsername(), request.getDeliveryManagerUsername());
            return UpdateDeliveryResponse.of(delivery);
        }).orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

    }

    @Transactional
    public void deleteDelivery(UUID deliveryUUID, String deleteBy) {

        deliveryRepository.findByUuidAndIsDeletedFalse(deliveryUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND))
                .delete(deleteBy);

        Predicate predicate = QDeliveryRoute.deliveryRoute.deliveryUUID.eq(deliveryUUID);
        deliveryRouteService.searchDeliveryRoutesIsDeletedFalse(predicate, Pageable.unpaged())
                .getContent()
                .forEach(deliveryRouteResponse -> {
                    deliveryRouteService.deleteDeliveryRoute(deliveryRouteResponse.getUuid(), deleteBy);
                });

    }

    public PagedModel<DeliveryResponse> searchDeliveriesIsDeletedFalse(Predicate predicate, Pageable pageable) {
        Page<Delivery> deliveryPage = deliveryRepository.searchDeliveriesIsDeletedFalse(predicate, pageable);
        return new PagedModel<>(
                new PageImpl<>(
                        deliveryPage.getContent().stream()
                                .map(DeliveryResponse::of)
                                .toList(),
                        deliveryPage.getPageable(),
                        deliveryPage.getTotalElements()
                )
        );
    }

    public DeliveryResponse findDeliveryByUUID(UUID deliveryUUID) {
        return deliveryRepository.findByUuidAndIsDeletedFalse(deliveryUUID)
                .map(DeliveryResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));
    }
}
