package com.sparta.msa.delivery.presentation.request;

import com.sparta.msa.delivery.application.dto.delivery.UpdateDeliveryDto;
import com.sparta.msa.delivery.domain.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateDeliveryRequest {
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

    @NotNull
    @Size(max = 10)
    private String deliveryManagerUsername;

    public UpdateDeliveryDto toDto() {
        return UpdateDeliveryDto.of(this.orderUUID, this.status, this.departureHubUUID,
                this.arrivalHubUUID, this.deliveryAddress, this.recipientUsername, this.deliveryManagerUsername);
    }
}
