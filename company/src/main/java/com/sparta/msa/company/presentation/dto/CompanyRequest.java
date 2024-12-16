package com.sparta.msa.company.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.company.application.dto.CompanyDto;
import com.sparta.msa.company.domain.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class CompanyRequest implements Serializable {
    private String name;
    private Type type;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID hubUUID;
    private String address;

    public CompanyDto toDto() { return CompanyDto.of(this.name, this.type, this.hubUUID, this.address); }
}
