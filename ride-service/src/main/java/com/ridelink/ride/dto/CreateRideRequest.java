package com.ridelink.ride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateRideRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotBlank(message = "Destination location is required")
    private String destinationLocation;

    @Positive(message = "Distance must be positive")
    private Double estimatedDistanceKm;

    private boolean autoAssignDriver; // Optional flag to automatically find and assign an available driver

    public CreateRideRequest() {}

    public CreateRideRequest(Long passengerId, String pickupLocation, String destinationLocation, Double estimatedDistanceKm) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.estimatedDistanceKm = estimatedDistanceKm;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public Double getEstimatedDistanceKm() {
        return estimatedDistanceKm;
    }

    public void setEstimatedDistanceKm(Double estimatedDistanceKm) {
        this.estimatedDistanceKm = estimatedDistanceKm;
    }

    public boolean isAutoAssignDriver() {
        return autoAssignDriver;
    }

    public void setAutoAssignDriver(boolean autoAssignDriver) {
        this.autoAssignDriver = autoAssignDriver;
    }
}
