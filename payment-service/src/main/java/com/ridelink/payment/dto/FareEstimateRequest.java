package com.ridelink.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FareEstimateRequest {

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotBlank(message = "Destination location is required")
    private String destinationLocation;

    @Positive(message = "Distance must be greater than 0")
    private Double estimatedDistanceKm;

    private Boolean peakHour;
    private String couponCode;

    public FareEstimateRequest() {}

    public FareEstimateRequest(String pickupLocation, String destinationLocation, Double estimatedDistanceKm) {
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.estimatedDistanceKm = estimatedDistanceKm;
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

    public Boolean getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(Boolean peakHour) {
        this.peakHour = peakHour;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
