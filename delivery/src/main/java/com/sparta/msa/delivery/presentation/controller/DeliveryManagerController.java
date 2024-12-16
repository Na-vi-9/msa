package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.application.service.DeliveryManagerService;
import com.sparta.msa.delivery.domain.model.DeliveryManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.querydsl.core.types.Predicate;


@RestController
@RequestMapping("/delivery/managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    // 배송 담당자 추가
    @PostMapping
    public ResponseEntity<DeliveryManagerResponse> addDeliveryManager(@Valid @RequestBody DeliveryManagerRequest request) {
        DeliveryManagerResponse response = deliveryManagerService.addDeliveryManager(request);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 배정
    @PostMapping("/assign")
    public ResponseEntity<DeliveryManagerResponse> assignDeliveryManager() {
        DeliveryManagerResponse response = deliveryManagerService.assignDeliveryManager();
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 수정
    @PutMapping("/{username}")
    public ResponseEntity<DeliveryManagerResponse> updateDeliveryManager(
            @PathVariable String username,
            @Valid @RequestBody DeliveryManagerRequest request
    ) {
        DeliveryManagerResponse response = deliveryManagerService.updateDeliveryManager(username, request);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 삭제
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteDeliveryManager(@PathVariable String username) {
        deliveryManagerService.deleteDeliveryManager(username);
        return ResponseEntity.noContent().build();
    }

    // 배송 담당자 목록 조회 (검색 및 페이징 포함)
    @GetMapping
    public ResponseEntity<Page<DeliveryManagerResponse>> getDeliveryManagers(
            @RequestParam(required = false) String keyword,
            @QuerydslPredicate(root = DeliveryManager.class) Predicate predicate,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<DeliveryManagerResponse> response = deliveryManagerService.getDeliveryManagers(keyword, predicate, pageable);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 상세 조회
    @GetMapping("/{username}")
    public ResponseEntity<DeliveryManagerResponse> getDeliveryManagerDetail(@PathVariable String username) {
        DeliveryManagerResponse response = deliveryManagerService.getDeliveryManagerDetail(username);
        return ResponseEntity.ok(response);
    }
}
