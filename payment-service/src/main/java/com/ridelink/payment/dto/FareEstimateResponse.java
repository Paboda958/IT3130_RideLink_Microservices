package com.ridelink.payment.dto;

public class FareEstimateResponse {

    private String pickupLocation;
    private String destinationLocation;
    private Double distanceKm;
    private Double baseFare;
    private Double distanceRate;
    private Double totalEstimatedFare;
    private Double surgeMultiplier = 1.0;
    private Double couponDiscount = 0.0;
    private String currency = "LKR";

    public FareEstimateResponse() {}

    public FareEstimateResponse(String pickupLocation, String destinationLocation, Double distanceKm,
                                Double baseFare, Double distanceRate, Double totalEstimatedFare) {
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.distanceRate = distanceRate;
        this.totalEstimatedFare = totalEstimatedFare;
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

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getDistanceRate() {
        return distanceRate;
    }

    public void setDistanceRate(Double distanceRate) {
        this.distanceRate = distanceRate;
    }

    public Double getTotalEstimatedFare() {
        return totalEstimatedFare;
    }

    public void setTotalEstimatedFare(Double totalEstimatedFare) {
        this.totalEstimatedFare = totalEstimatedFare;
    }

    public Double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(Double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
