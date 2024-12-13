package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @PostMapping("/assign")
    public ResponseEntity<DeliveryManagerResponse> assignDeliveryManager() {
        DeliveryManagerResponse response = deliveryManagerService.assignDeliveryManager();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<DeliveryManagerResponse> updateDeliveryManager(
            @PathVariable String username,
            @RequestBody DeliveryManagerRequest request
    ) {
        DeliveryManagerResponse response = deliveryManagerService.updateDeliveryManager(username, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteDeliveryManager(@PathVariable String username) {
        deliveryManagerService.deleteDeliveryManager(username);
        return ResponseEntity.noContent().build();
    }

    // 배송 담당자 목록 조회
    @GetMapping
    public ResponseEntity<List<DeliveryManagerResponse>> getDeliveryManagers(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String keyword
    ) {
        List<DeliveryManagerResponse> response = deliveryManagerService.getDeliveryManagers(condition, keyword);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 상세 조회
    @GetMapping("/{username}")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManagerDetail(@PathVariable String username) {
        DeliveryManagerResponse response = deliveryManagerService.getDeliveryManagerDetail(username);
        return ResponseEntity.ok(response);
    }

}
