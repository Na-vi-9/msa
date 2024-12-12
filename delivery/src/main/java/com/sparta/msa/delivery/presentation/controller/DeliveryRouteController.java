package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.deliveryRoute.UpdateDeliveryRouteResponse;
import com.sparta.msa.delivery.application.service.DeliveryRouteService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import com.sparta.msa.delivery.presentation.request.UpdateDeliveryRouteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/delivery/route")
@RequiredArgsConstructor
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;

    @PutMapping("/{deliveryRouteUUID}")
    public CommonResponse<UpdateDeliveryRouteResponse> updateDeliveryRoute(@PathVariable UUID deliveryRouteUUID,
                                                                           @Valid @RequestBody UpdateDeliveryRouteRequest request) {

        return CommonResponse.ofSuccess(deliveryRouteService.updateDeliveryRoute(deliveryRouteUUID, request.toDto()));

    }
}
