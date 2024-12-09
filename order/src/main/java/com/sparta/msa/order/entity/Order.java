package com.sparta.msa.order.entity;

import com.sparta.msa.order.dto.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
@Table(name = "p_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "supplier_company_id", nullable = false)
    private Long supplierCompanyId;

    @Column(name = "receiver_company_id", nullable = false)
    private Long receiverCompanyId;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "delivery_id", nullable = false)
    private Long deliveryId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 500)
    private String memo;

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

    // 엔티티 생성 메서드
    public static Order createOrder(CreateOrderRequest request, Long supplierCompanyId, Long receiverCompanyId, Long productId, Long deliveryId) {
        Order order = new Order();
        order.uuid = UUID.randomUUID();
        order.orderId = System.currentTimeMillis(); // 임시로 고유한 값 생성
        order.supplierCompanyId = supplierCompanyId;
        order.receiverCompanyId = receiverCompanyId;
        order.productId = productId;
        order.deliveryId = deliveryId;
        order.quantity = request.getQuantity();
        order.memo = request.getMemo();
        order.createdBy = "system"; // 유저 도메인 구현 전 임시값
        order.updatedBy = "system"; // 유저 도메인 구현 전 임시값
        return order;
    }

    // soft-delete
    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}
