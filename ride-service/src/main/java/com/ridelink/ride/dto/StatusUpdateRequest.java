package com.ridelink.ride.dto;

import com.ridelink.ride.model.RideStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {

    @NotNull(message = "Target status is required")
    private RideStatus status;

    private String cancellationReason;

    public StatusUpdateRequest() {}

    public StatusUpdateRequest(RideStatus status) {
        this.status = status;
    }

    public StatusUpdateRequest(RideStatus status, String cancellationReason) {
        this.status = status;
        this.cancellationReason = cancellationReason;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
