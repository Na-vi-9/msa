package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerRequest;
import com.sparta.msa.delivery.application.dto.deliveryManager.DeliveryManagerResponse;
import com.sparta.msa.delivery.application.service.DeliveryManagerService;
import com.sparta.msa.delivery.infrastructure.exception.CustomException;
import com.sparta.msa.delivery.infrastructure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/delivery/managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    private boolean isMaster(String role) {
        return "MASTER".equals(role);
    }

    private boolean isManagerOrMaster(String role) {
        return "MASTER".equals(role) || "HUB_MANAGER".equals(role);
    }

    // 배송 담당자 생성: MASTER, HUB_MANAGER만 가능
    @PostMapping
    public ResponseEntity<DeliveryManagerResponse> addDeliveryManager(
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Role") String role,
            @RequestBody DeliveryManagerRequest request
    ) {
        if (!isManagerOrMaster(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        DeliveryManagerResponse response = deliveryManagerService.addDeliveryManager(request);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 수정: MASTER, HUB_MANAGER만 가능
    @PutMapping("/{username}")
    public ResponseEntity<DeliveryManagerResponse> updateDeliveryManager(
            @PathVariable String username,
            @RequestHeader("X-Username") String requestUsername,
            @RequestHeader("X-Role") String role,
            @RequestBody DeliveryManagerRequest request
    ) {
        if (!isManagerOrMaster(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        DeliveryManagerResponse response = deliveryManagerService.updateDeliveryManager(username, request);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 삭제: MASTER만 가능
    @DeleteMapping("/{DeliveryUUID}")
    public ResponseEntity<?> deleteDelivery(
            @PathVariable UUID DeliveryUUID,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Role") String role
    ) {
        if (!isMaster(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        deliveryManagerService.deleteDeliveryManager(username);
        return ResponseEntity.ok("배송 담당자 삭제 완료");
    }

    // 배송 담당자 배정: MASTER만 가능
    @PutMapping("/assign")
    public ResponseEntity<?> assignDeliveryManager(
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Role") String role
    ) {
        if (!isMaster(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        return ResponseEntity.ok("배송 담당자 배정 성공");
    }

    // 특정 허브의 업체 배송 담당자 배정: MASTER만 가능
    @PostMapping("/assign/company/{hubUUID}")
    public ResponseEntity<?> assignCompanyDeliveryManager(
            @PathVariable UUID hubUUID,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Role") String role
    ) {
        if (!isMaster(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        deliveryManagerService.assignCompanyDeliveryManager(hubUUID);
        return ResponseEntity.ok("업체 배송 담당자 배정 성공");
    }
}
