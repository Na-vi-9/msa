package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.DeliveryCreateResponse;
import com.sparta.msa.delivery.application.dto.DeliveryResponse;
import com.sparta.msa.delivery.application.service.DeliveryService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import com.sparta.msa.delivery.presentation.request.DeliveryCreateRequest;
import com.sparta.msa.delivery.presentation.request.DeliveryUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public CommonResponse<DeliveryCreateResponse> createDelivery(@Valid @RequestBody DeliveryCreateRequest request) {
        return CommonResponse.ofSuccess(null);
    }

    @GetMapping
    public CommonResponse<Page<DeliveryResponse>> findDeliveries(@PageableDefault(sort = {"created_at", "updated_at"}) Pageable pageable,
                                                                 @RequestParam String condition,
                                                                 @RequestParam String keyword) {
        return CommonResponse.ofSuccess(null);
    }

    @GetMapping("/{DeliveryUUID}")
    public CommonResponse<DeliveryResponse> findDeliveryByUUID(@PathVariable String DeliveryUUID) {
        return CommonResponse.ofSuccess(null);
    }

    @PutMapping("/{DeliveryUUID}")
    public CommonResponse<DeliveryResponse> updateDelivery(@PathVariable String DeliveryUUID,
                                                           @Valid @RequestBody DeliveryUpdateRequest request) {
        return CommonResponse.ofSuccess(null);
    }

    @DeleteMapping("/{DeliveryUUID}")
    public CommonResponse<?> deleteDelivery(@PathVariable String DeliveryUUID) {
        return CommonResponse.ofSuccess(null);
    }
}
