package com.sparta.msa.company.domain.entity;

import com.sparta.msa.company.domain.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_company")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Company extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyUUID;

    private String name;

    private Type type;

    private UUID manageHubUUID;

    private String address;

    public static Company create(String name, Type type, UUID manageHubUUID, String address) {

        return Company.builder()
                .name(name)
                .type(type)
                .manageHubUUID(manageHubUUID)
                .address(address)
                .build();
    }

    public void update(String name, Type type, UUID manageHubUUID, String address) {
        this.name = name;
        this.type = type;
        this.manageHubUUID = manageHubUUID;
        this.address = address;
    }
}
