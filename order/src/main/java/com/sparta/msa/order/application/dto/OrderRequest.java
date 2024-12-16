package com.sparta.msa.order.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderRequest {
    @NotNull(message = "공급 업체 UUID는 필수입니다.")
    private UUID supplierCompanyUUID;

    @NotNull(message = "수령 업체 UUID는 필수입니다.")
    private UUID receiverCompanyUUID;

    @NotNull(message = "상품 UUID는 필수입니다.")
    private UUID productUUID;

    @Positive(message = "수량은 1 이상이어야 합니다.")
    private Integer quantity;

    private String memo;
}
