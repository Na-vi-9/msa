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
    private UUID supplierCompanyUUID;

    @Column(name = "receiver_company_id", nullable = false)
    private UUID receiverCompanyUUID;

    @Column(name = "product_id", nullable = false)
    private UUID productUUID;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryUUID;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 500)
    private String memo;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "is_canceled", nullable = false)
    private boolean isCanceled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(length = 500)
    private String finalDeadline; // AI에서 생성된 최종 발송 시한

    @Builder
    public static Order createOrder(OrderRequest request, UUID supplierCompanyUUID, UUID receiverCompanyUUID,
                                    UUID productUUID, UUID deliveryUUID, String createdBy, String finalDeadline) {
        return Order.builder()
                .uuid(UUID.randomUUID())
                .supplierCompanyUUID(supplierCompanyUUID)
                .receiverCompanyUUID(receiverCompanyUUID)
                .productUUID(productUUID)
                .quantity(request.getQuantity())
                .memo(request.getMemo())
                .deliveryUUID(deliveryUUID)
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .finalDeadline(finalDeadline) // AI 발송 시한 설정
                .build();
    }

    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

    public void updateOrder(UUID supplierCompanyUUID, UUID receiverCompanyUUID, UUID productUUID,
                            Integer quantity, String memo, String updatedBy) {
        this.supplierCompanyUUID = supplierCompanyUUID;
        this.receiverCompanyUUID = receiverCompanyUUID;
        this.productUUID = productUUID;
        this.quantity = quantity;
        this.memo = memo;
        setUpdatedBy(updatedBy);
    }

    public void cancel(String updatedBy) {
        this.isCanceled = true;
        setUpdatedBy(updatedBy);
    }

    private void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}
