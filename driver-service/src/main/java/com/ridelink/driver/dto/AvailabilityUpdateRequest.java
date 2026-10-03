package com.ridelink.driver.dto;

import com.ridelink.driver.model.DriverAvailability;
import jakarta.validation.constraints.NotNull;

public class AvailabilityUpdateRequest {

    @NotNull(message = "Availability status is required")
    private DriverAvailability availabilityStatus;

    public AvailabilityUpdateRequest() {}

    public AvailabilityUpdateRequest(DriverAvailability availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public DriverAvailability getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(DriverAvailability availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
