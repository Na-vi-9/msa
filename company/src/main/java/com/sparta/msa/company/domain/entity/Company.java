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

    @Enumerated(EnumType.STRING)
    private Type type;

    private UUID manageHubUUID;

    private String address;

    private String managerName;

    public static Company create(String name, Type type, UUID manageHubUUID, String address, String managerName) {

        return Company.builder()
                .name(name)
                .type(type)
                .manageHubUUID(manageHubUUID)
                .address(address)
                .managerName(managerName)
                .build();
    }

    public void update(String name, Type type, UUID manageHubUUID, String address, String managerName) {
        this.name = name;
        this.type = type;
        this.manageHubUUID = manageHubUUID;
        this.address = address;
        this.managerName = managerName;
    }
}
