package com.sparta.msa.delivery.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.deliveryRoute.DeliveryRouteResponse;
import com.sparta.msa.delivery.application.dto.deliveryRoute.UpdateDeliveryRouteResponse;
import com.sparta.msa.delivery.application.service.DeliveryRouteService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.presentation.request.UpdateDeliveryRouteRequest;
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
@RequestMapping("/delivery/route")
@RequiredArgsConstructor
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;

    @PutMapping("/{deliveryRouteUUID}")
    public CommonResponse<UpdateDeliveryRouteResponse> updateDeliveryRoute(@PathVariable UUID deliveryRouteUUID,
                                                                           @Valid @RequestBody UpdateDeliveryRouteRequest request) {

        return CommonResponse.ofSuccess(deliveryRouteService.updateDeliveryRoute(deliveryRouteUUID, request.toDto()));

    }

    @GetMapping
    public CommonResponse<PagedModel<DeliveryRouteResponse>> searchDeliveryRoutes(@QuerydslPredicate(root = DeliveryRoute.class) Predicate predicate,
                                                                         @PageableDefault(sort = {"createdAt", "updatedAt"}) Pageable pageable) {
        return CommonResponse.ofSuccess(deliveryRouteService.searchDeliveryRoutesIsDeletedFalse(predicate, pageable));
    }

    @GetMapping("/{DeliveryRouteUUID}")
    public CommonResponse<DeliveryRouteResponse> findDeliveryRouteByUUID(@PathVariable UUID DeliveryRouteUUID) {
        return CommonResponse.ofSuccess(deliveryRouteService.findDeliveryRouteByUUID(DeliveryRouteUUID));
    }
}
