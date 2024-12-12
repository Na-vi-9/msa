package com.sparta.msa.delivery.presentation.request;

import com.sparta.msa.delivery.application.dto.CreateDeliveryDto;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateDeliveryRequest {
    @NotNull
    private UUID orderUUID;

    @NotNull
    private DeliveryStatus status;

    @NotNull
    private UUID departureHubUUID;

    @NotNull
    private UUID arrivalHubUUID;

    @NotNull
    @Size(max = 255)
    private String deliveryAddress;

    @NotNull
    @Size(max = 10)
    private String recipientUsername;

    public CreateDeliveryDto toDto() {
        return CreateDeliveryDto.create(this.orderUUID, this.status, this.departureHubUUID,
                                    this.arrivalHubUUID, this.deliveryAddress, this.recipientUsername);
    }
}
