package com.sparta.msa.hub.application.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.hub.domain.entity.Hub;
import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetHubsResponse implements Serializable{

    private HubPage hubPage;

    public static GetHubsResponse of(Page<Hub> hubPage) {
        return GetHubsResponse.builder()
                .hubPage(HubPage.from(hubPage))
                .build();
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HubPage implements Serializable{
        private List<Hub> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;

        public static HubPage from(Page<com.sparta.msa.hub.domain.entity.Hub> hubPage) {
            return
                    new  HubPage(
                            HubPage.Hub.from(hubPage.getContent()),
                            hubPage.getPageable().getPageNumber(),
                            hubPage.getPageable().getPageSize(),
                            hubPage.getTotalElements()
                    );
        }


        @Getter
        @Builder
        @AllArgsConstructor
        public static class Hub {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private UUID hubUUID;

            private String hubName;
            private String hubAddress;
            private Double latitude;
            private Double longitude;
            private Long managerId;

            public static List<Hub> from(List<com.sparta.msa.hub.domain.entity.Hub> hubList) {
                return hubList.stream()
                        .map(HubPage.Hub::from)
                        .toList();
            }

            public static Hub from(com.sparta.msa.hub.domain.entity.Hub hub) {
                return Hub.builder()
                        .hubUUID(hub.getHubUUID())
                        .hubName(hub.getName())
                        .hubAddress(hub.getAddress())
                        .latitude(hub.getLatitude())
                        .longitude(hub.getLongitude())
                        .managerId(hub.getManagerId())
                        .build();
            }
        }
    }
}
