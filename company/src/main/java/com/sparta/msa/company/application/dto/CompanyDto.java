package com.sparta.msa.company.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.company.domain.enums.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CompanyDto implements Serializable {
    private String name;
    private Type type;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID hubUUID;
    private String address;
    private String managerName;

    public static CompanyDto of(String name, Type type, UUID hubUUID, String address, String managerName) {
        return CompanyDto.builder()
                .name(name)
                .type(type)
                .hubUUID(hubUUID)
                .address(address)
                .managerName(managerName)
                .build();
    }
}
