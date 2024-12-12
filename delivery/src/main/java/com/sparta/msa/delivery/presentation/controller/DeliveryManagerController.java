package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/delivery/managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping
    public ResponseEntity<DeliveryManagerResponse> addDeliveryManager(@RequestBody DeliveryManagerRequest request) {
        DeliveryManagerResponse response = deliveryManagerService.addDeliveryManager(request);
        return ResponseEntity.ok(response);
    }

}
