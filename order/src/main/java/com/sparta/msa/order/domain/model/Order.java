package com.sparta.msa.order.domain.model;

import com.sparta.msa.order.application.dto.OrderRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "p_order")
public class Order {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "supplier_company_id", nullable = false)
    private UUID supplierCompanyId;

    @Column(name = "receiver_company_id", nullable = false)
    private UUID receiverCompanyId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 500)
    private String memo;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy = "system";

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public static Order createOrder(OrderRequest request, UUID supplierCompanyId, UUID receiverCompanyId, UUID productId, UUID deliveryId) {
        return Order.builder()
                .uuid(UUID.randomUUID())
                .supplierCompanyId(supplierCompanyId)
                .receiverCompanyId(receiverCompanyId)
                .productId(productId)
                .deliveryId(deliveryId)
                .quantity(request.getQuantity())
                .memo(request.getMemo())
                .createdAt(LocalDateTime.now())
                .createdBy("system") // 임시
                .updatedBy("system") // 임시
                .build();
    }

    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public void updateOrder(UUID supplierCompanyId, UUID receiverCompanyId, UUID productId, Integer quantity, String memo, String updatedBy) {
        this.supplierCompanyId = supplierCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.productId = productId;
        this.quantity = quantity;
        this.memo = memo;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}
