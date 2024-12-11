package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.CreateDeliveryResponse;
import com.sparta.msa.delivery.application.dto.UpdateDeliveryResponse;
import com.sparta.msa.delivery.application.service.DeliveryService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import com.sparta.msa.delivery.presentation.request.CreateDeliveryRequest;
import com.sparta.msa.delivery.presentation.request.UpdateDeliveryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public CommonResponse<Page<UpdateDeliveryResponse>> findDeliveries(@PageableDefault(sort = {"created_at", "updated_at"}) Pageable pageable,
                                                                       @RequestParam String condition,
                                                                       @RequestParam String keyword) {
        return CommonResponse.ofSuccess(null);
    }

    @GetMapping("/{DeliveryUUID}")
    public CommonResponse<UpdateDeliveryResponse> findDeliveryByUUID(@PathVariable UUID DeliveryUUID) {
        return CommonResponse.ofSuccess(null);
    }

    @PutMapping("/{DeliveryUUID}")
    public CommonResponse<UpdateDeliveryResponse> updateDelivery(@PathVariable UUID DeliveryUUID,
                                                                 @Valid @RequestBody UpdateDeliveryRequest request) {

        return CommonResponse.ofSuccess(deliveryService.updateDelivery(DeliveryUUID, request.toDto()));
    }

    @DeleteMapping("/{DeliveryUUID}")
    public CommonResponse<?> deleteDelivery(@PathVariable UUID DeliveryUUID) {
        return CommonResponse.ofSuccess(null);
    }
}
