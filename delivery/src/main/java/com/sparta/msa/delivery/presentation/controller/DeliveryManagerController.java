package com.sparta.msa.delivery.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.application.service.DeliveryManagerService;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

    @GetMapping("/{username}")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManagerDetail(@PathVariable String username) {
        DeliveryManagerResponse response = deliveryManagerService.getDeliveryManagerDetail(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryManagerResponse>> searchDeliveryManagers(
            @QuerydslPredicate(root = DeliveryManager.class) Predicate predicate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort) {
        Page<DeliveryManagerResponse> response = deliveryManagerService.searchDeliveryManagers(
                predicate, page, size, sort);
        return ResponseEntity.ok(response);
    }
}
