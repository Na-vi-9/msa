package com.sparta.msa.hub.application.service;

import com.sparta.msa.hub.application.dto.HubRouteInfo;
import com.sparta.msa.hub.application.dto.HubRouteResponse;
import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.domain.entity.HubRoute;
import com.sparta.msa.hub.domain.exception.CustomException;
import com.sparta.msa.hub.domain.exception.ErrorCode;
import com.sparta.msa.hub.infrastructure.repository.HubRepository;
import com.sparta.msa.hub.infrastructure.repository.HubRouteRepository;
import com.sparta.msa.hub.presentation.dto.HubInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouteService {

    private final HubRouteRepository hubRouteRepository;
    private final HubRepository hubRepository;
    private final RestTemplate restTemplate;

    @Value("${service.api.key}")
    private String apiKey;

    // TODO: repository 중복 코드 메서드로 최소화 하기 + 검증 메서드 만들기
    public void calculateAndSaveAllHubRoutes() {
        List<Hub> hubs = hubRepository.findAll();

        for (int i = 0; i < hubs.size(); i++) {
            for (int j = i + 1; j < hubs.size(); j++) {
                UUID departureHubUUID = hubs.get(i).getHubUUID();
                UUID arrivalHubUUID = hubs.get(j).getHubUUID();

                calculateAndSaveHubRoute(departureHubUUID, arrivalHubUUID);
            }
        }
    }

    public HubRouteResponse getHubRoute(UUID hubRouteUUID) {
        HubRoute hubRoute = hubRouteRepository.findByHubRouteUUIDAndIsDeletedIsFalse(hubRouteUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB_ROUTE, hubRouteUUID));

        return HubRouteResponse.from(hubRoute);
    }

    public List<HubRouteResponse> getAllHubRoute() {
        List<HubRoute> hubRoutes = hubRouteRepository.findAll();
        return hubRoutes.stream()
                .map(HubRouteResponse::from)
                .collect(Collectors.toList());
    }

    public HubRouteResponse updateHubRoute(UUID hubRouteUUID) {
        HubRoute hubRoute = hubRouteRepository.findByHubRouteUUIDAndIsDeletedIsFalse(hubRouteUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB_ROUTE, hubRouteUUID));

        Hub departureHub = hubRepository.findByHubUUID(hubRoute.getDepartureHubUUID())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, hubRoute.getDepartureHubUUID()));
        Hub arrivalHub = hubRepository.findByHubUUID(hubRoute.getArrivalHubUUID())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, hubRoute.getArrivalHubUUID()));

        String url = "https://api.openrouteservice.org/v2/directions/driving-car";
        String response = callApi(url, departureHub.getLatitude(), departureHub.getLongitude(),
                arrivalHub.getLatitude(), arrivalHub.getLongitude());

        double distance = extractDistanceFromGeoJson(response);
        double duration = extractDurationFromGeoJson(response);

        hubRoute.update((int) duration, distance);

        return HubRouteResponse.from(hubRoute);
    }

    public void deleteHubRoute(UUID hubRouteUUID) {
        HubRoute hubRoute = hubRouteRepository.findByHubRouteUUIDAndIsDeletedIsFalse(hubRouteUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB_ROUTE, hubRouteUUID));

        // TODO: DeletedUsername 추가되도록 변경
        String deletedManagerName = "master";

        hubRoute.markDeleted(deletedManagerName);
    }

    public HubRouteInfo getRouteByDepartureAndArrival(HubInfoRequest hubInfoRequest) {
        HubRoute hubRoute = hubRouteRepository
                .findByDepartureHubUUIDAndArrivalHubUUIDAndIsDeletedIsFalse(
                        hubInfoRequest.getDepartureHubUUID(), hubInfoRequest.getArrivalHubUUID())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB_ROUTE));

        return HubRouteInfo.from(hubRoute);
    }

    public HubRoute calculateAndSaveHubRoute(UUID departureHubUUID, UUID arrivalHubUUID) {
        HubRoute existingRoute = hubRouteRepository
                .findByDepartureHubUUIDAndArrivalHubUUIDAndIsDeletedIsFalse(departureHubUUID, arrivalHubUUID)
                .orElse(null);

        if (existingRoute != null) {
            return existingRoute;
        }

        Hub departureHub = hubRepository.findByHubUUID(departureHubUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, departureHubUUID.toString()));
        Hub arrivalHub = hubRepository.findByHubUUID(arrivalHubUUID)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HUB, arrivalHubUUID.toString()));

        String url = "https://api.openrouteservice.org/v2/directions/driving-car";
        String response = callApi(url, departureHub.getLatitude(), departureHub.getLongitude(),
                arrivalHub.getLatitude(), arrivalHub.getLongitude());

        double distance = extractDistanceFromGeoJson(response);
        double duration = extractDurationFromGeoJson(response);

        HubRoute hubRoute = HubRoute.create(departureHubUUID, arrivalHubUUID, (int) duration, distance);

        return hubRouteRepository.save(hubRoute);
    }

    private String callApi(String url, Double departureLat, Double departureLong, Double arrivalLat, Double arrivalLong) {
        String apiUrl = String.format("%s?api_key=%s&start=%f,%f&end=%f,%f", url,
                apiKey, departureLong, departureLat, arrivalLong, arrivalLat);
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response.getBody();
    }

    private double extractDistanceFromGeoJson(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray features = jsonResponse.getJSONArray("features");
            JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
            return properties.getJSONArray("segments").getJSONObject(0).getDouble("distance");
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage(), e);
            return 0;
        }
    }

    private double extractDurationFromGeoJson(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray features = jsonResponse.getJSONArray("features");
            JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
            return properties.getJSONArray("segments").getJSONObject(0).getDouble("duration");
        } catch (Exception e) {
            log.error("에러 발생: {}", e.getMessage(), e);
            return 0;
        }
    }
}
