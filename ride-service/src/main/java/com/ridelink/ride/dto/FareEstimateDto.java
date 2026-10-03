package com.ridelink.ride.dto;

public class FareEstimateDto {
    private String pickupLocation;
    private String destinationLocation;
    private Double distanceKm;
    private Double totalEstimatedFare;

    public FareEstimateDto() {}

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

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getTotalEstimatedFare() {
        return totalEstimatedFare;
    }

    public void setTotalEstimatedFare(Double totalEstimatedFare) {
        this.totalEstimatedFare = totalEstimatedFare;
    }
}
