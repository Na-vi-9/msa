package com.sparta.msa.hub.infrastructure.config;

import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.infrastructure.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HubDataLoader implements CommandLineRunner {

    private final HubRepository hubRepository;

    @Override
    public void run(String... args) throws Exception {
        if (hubRepository.count() == 0) {
            hubRepository.saveAll(
                    List.of(
                            Hub.create("서울특별시 센터",
                                    "서울특별시 송파구 송파대로 55",
                                    37.514574,
                                    127.112236,
                                    "master"),
                            Hub.create("경기 북부 센터",
                                    "경기도 고양시 덕양구 권율대로 570",
                                    37.658347,
                                    126.832010,
                                    "master"),
                            Hub.create("경기 남부 센터",
                                    "경기도 이천시 덕평로 257-21",
                                    37.277902,
                                    127.460717,
                                    "master"),
                            Hub.create("부산광역시 센터",
                                    "부산 동구 중앙대로 206",
                                    35.107903,
                                    129.040270,
                                    "master"),
                            Hub.create("대구광역시 센터",
                                    "대구 북구 태평로 161",
                                    35.872289,
                                    128.602153,
                                    "master"),
                            Hub.create("인천광역시 센터",
                                    "인천 남동구 정각로 29",
                                    37.456256,
                                    126.705017,
                                    "master"),
                            Hub.create("광주광역시 센터",
                                    "광주 서구 내방로 111",
                                    35.159523,
                                    126.852556,
                                    "master"),
                            Hub.create("대전광역시 센터",
                                    "대전 서구 둔산로 100",
                                    36.350412,
                                    127.384547,
                                    "master"),
                            Hub.create("울산광역시 센터",
                                    "울산 남구 중앙로 201",
                                    35.539352,
                                    129.314286,
                                    "master"),
                            Hub.create("세종특별자치시 센터",
                                    "세종특별자치시 한누리대로 2130",
                                    36.480033,
                                    127.289347,
                                    "master"),
                            Hub.create("강원특별자치도 센터",
                                    "강원특별자치도 춘천시 중앙로 1",
                                    37.880932,
                                    127.727033,
                                    "master"),
                            Hub.create("충청북도 센터",
                                    "충북 청주시 상당구 상당로 82",
                                    36.635561,
                                    127.491733,
                                    "master"),
                            Hub.create("충청남도 센터",
                                    "충남 홍성군 홍북읍 충남대로 21",
                                    36.654344,
                                    126.680501,
                                    "master"),
                            Hub.create("전북특별자치도 센터",
                                    "전북특별자치도 전주시 완산구 효자로 225",
                                    35.822643,
                                    127.148699,
                                    "master"),
                            Hub.create("전라남도 센터",
                                    "전남 무안군 삼향읍 오룡길 1",
                                    34.973538,
                                    126.732119,
                                    "master"),
                            Hub.create("경상북도 센터",
                                    "경북 안동시 풍천면 도청대로 455",
                                    36.566107,
                                    128.729467,
                                    "master"),
                            Hub.create("경상남도 센터",
                                    "경남 창원시 의창구 중앙대로 300",
                                    35.230660,
                                    128.682355,
                                    "master")
                    )
            );
        }
    }
}
