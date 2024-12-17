package com.sparta.msa.company.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
public class CreateCompanyResponse implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID companyUUID;

    public static CreateCompanyResponse from(UUID companyUUID) {
        return CreateCompanyResponse.builder()
                .companyUUID(companyUUID).build();
    }
}
