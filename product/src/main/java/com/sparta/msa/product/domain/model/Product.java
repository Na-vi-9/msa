package com.sparta.msa.product.domain.model;

import com.sparta.msa.product.application.dto.CreateProductRequest;
import com.sparta.msa.product.application.dto.UpdateProductRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_product")
public class Product {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "company_id", nullable = false)
    private UUID companyUUID;

    @Column(name = "hub_uuid", nullable = false)
    private UUID hubUUID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public Product(UUID uuid, UUID companyUUID, UUID hubUUID, String name, Integer quantity, String createdBy) {
        this.uuid = uuid;
        this.companyUUID = companyUUID;
        this.hubUUID = hubUUID;
        this.name = name;
        this.quantity = quantity;
        this.createdBy = createdBy;
    }

    public static Product create(CreateProductRequest request, String createdBy) {
        return Product.builder()
                .uuid(UUID.randomUUID())
                .companyUUID(request.getCompanyUUID())
                .hubUUID(request.getHubUUID())
                .name(request.getName())
                .quantity(request.getQuantity())
                .createdBy(createdBy)
                .build();
    }

    public void update(UpdateProductRequest request, String updatedBy) {
        this.name = request.getName();
        this.quantity = request.getQuantity();
        this.updatedBy = updatedBy;
    }


    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
