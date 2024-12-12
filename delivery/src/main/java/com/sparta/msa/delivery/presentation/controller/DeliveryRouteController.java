package com.sparta.msa.delivery.presentation.controller;

import com.sparta.msa.delivery.application.service.DeliveryRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/delivery/route")
@RequiredArgsConstructor
public class DeliveryRouteController {
    private final DeliveryRouteService deliveryRouteService;

}
