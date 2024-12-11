package com.sparta.msa.delivery.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.CreateDeliveryResponse;
import com.sparta.msa.delivery.application.dto.DeliveryResponse;
import com.sparta.msa.delivery.application.dto.UpdateDeliveryResponse;
import com.sparta.msa.delivery.application.service.DeliveryService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import com.sparta.msa.delivery.domain.model.Delivery;
import com.sparta.msa.delivery.presentation.request.CreateDeliveryRequest;
import com.sparta.msa.delivery.presentation.request.UpdateDeliveryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public CommonResponse<CreateDeliveryResponse> createDelivery(@Valid @RequestBody CreateDeliveryRequest request) {
        return CommonResponse.ofSuccess(deliveryService.createDelivery(request.toDto()));
    }

    @GetMapping
    public CommonResponse<PagedModel<DeliveryResponse>> searchDeliveries(@QuerydslPredicate(root = Delivery.class) Predicate predicate,
                                                                         @PageableDefault(sort = {"createdAt", "updatedAt"}) Pageable pageable) {
        return CommonResponse.ofSuccess(deliveryService.searchDeliveriesIsDeletedFalse(predicate, pageable));
    }

    @GetMapping("/{DeliveryUUID}")
    public CommonResponse<DeliveryResponse> findDeliveryByUUID(@PathVariable String DeliveryUUID) {
        return CommonResponse.ofSuccess(null);
    }

    @PutMapping("/{DeliveryUUID}")
    public CommonResponse<UpdateDeliveryResponse> updateDelivery(@PathVariable UUID DeliveryUUID,
                                                                 @Valid @RequestBody UpdateDeliveryRequest request) {

        return CommonResponse.ofSuccess(deliveryService.updateDelivery(DeliveryUUID, request.toDto()));
    }

    @DeleteMapping("/{DeliveryUUID}")
    public CommonResponse<?> deleteDelivery(@PathVariable UUID DeliveryUUID) {
        String deletedBy = "tmp";
        deliveryService.deleteDelivery(DeliveryUUID, deletedBy);

        return CommonResponse.ofSuccess(null);
    }
}
