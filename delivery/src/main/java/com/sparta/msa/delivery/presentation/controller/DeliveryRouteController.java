package com.sparta.msa.delivery.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.sparta.msa.delivery.application.dto.deliveryRoute.DeliveryRouteResponse;
import com.sparta.msa.delivery.application.dto.deliveryRoute.UpdateDeliveryRouteResponse;
import com.sparta.msa.delivery.application.service.DeliveryRouteService;
import com.sparta.msa.delivery.common.dto.CommonResponse;
import com.sparta.msa.delivery.domain.model.DeliveryRoute;
import com.sparta.msa.delivery.infrastructure.exception.CustomException;
import com.sparta.msa.delivery.infrastructure.exception.ErrorCode;
import com.sparta.msa.delivery.presentation.request.UpdateDeliveryRouteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/delivery/route")
@RequiredArgsConstructor
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;

    @PutMapping("/{deliveryRouteUUID}")
    public CommonResponse<UpdateDeliveryRouteResponse> updateDeliveryRoute(@PathVariable UUID deliveryRouteUUID,
                                                                           @Valid @RequestBody UpdateDeliveryRouteRequest request,
                                                                           @RequestHeader(value = "X-Role") String role) {
        if(!role.equals("MASTER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        return CommonResponse.ofSuccess(deliveryRouteService.updateDeliveryRoute(deliveryRouteUUID, request.toDto()));
    }

    @GetMapping
    public CommonResponse<PagedModel<DeliveryRouteResponse>> searchDeliveryRoutes(@QuerydslPredicate(root = DeliveryRoute.class) Predicate predicate,
                                                                         @PageableDefault(sort = {"createdAt", "updatedAt"}) Pageable pageable) {
        Pageable adjustedPageable = adjustPageSize(pageable, List.of(10, 30, 50), 10);

        return CommonResponse.ofSuccess(deliveryRouteService.searchDeliveryRoutesIsDeletedFalse(predicate, adjustedPageable));
    }

    @GetMapping("/{DeliveryRouteUUID}")
    public CommonResponse<DeliveryRouteResponse> findDeliveryRouteByUUID(@PathVariable UUID DeliveryRouteUUID) {
        return CommonResponse.ofSuccess(deliveryRouteService.findDeliveryRouteByUUID(DeliveryRouteUUID));
    }

    @DeleteMapping("/{deliveryRouteUUID}")
    public CommonResponse<?> deleteDeliveryRoute(@PathVariable UUID deliveryRouteUUID,
                                                 @RequestHeader(value = "X-Username") String username,
                                                 @RequestHeader(value = "X-Role") String role) {
        if(!role.equals("MASTER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        deliveryRouteService.deleteDeliveryRoute(deliveryRouteUUID, username);

        return CommonResponse.ofSuccess(null);
    }

    private Pageable adjustPageSize(Pageable pageable, List<Integer> allowSizes, int defaultSize) {
        if(!allowSizes.contains(pageable.getPageSize())) {
            return PageRequest.of(pageable.getPageNumber(), defaultSize, pageable.getSort());
        }
        return pageable;
    }
}
