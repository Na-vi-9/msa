package com.sparta.msa.company.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.company.domain.entity.Company;
import com.sparta.msa.company.domain.enums.Type;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class CompanyResponse implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID companyUUID;
    private String companyName;
    private Type companyType;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID manageHubUUID;
    private String companyAddress;
    private String managerName;

    public static CompanyResponse fromCompany(Company company) {
        return CompanyResponse.builder()
                .companyUUID(company.getCompanyUUID())
                .companyName(company.getName())
                .companyType(company.getType())
                .manageHubUUID(company.getManageHubUUID())
                .companyAddress(company.getAddress())
                .managerName(company.getManagerName())
                .build();
    }
}
