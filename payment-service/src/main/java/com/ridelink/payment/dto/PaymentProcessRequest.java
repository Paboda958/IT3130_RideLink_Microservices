package com.ridelink.payment.dto;

import com.ridelink.payment.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PaymentProcessRequest {

    @NotNull(message = "Ride ID is required")
    private Long rideId;

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    private Long driverId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private Double distanceKm;

    private Boolean peakHour;
    private String couponCode;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private boolean simulateFailure; // For testing negative payment scenarios

    public PaymentProcessRequest() {}

    public PaymentProcessRequest(Long rideId, Long passengerId, Long driverId, Double amount, Double distanceKm, PaymentMethod paymentMethod) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.amount = amount;
        this.distanceKm = distanceKm;
        this.paymentMethod = paymentMethod;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isSimulateFailure() {
        return simulateFailure;
    }

    public void setSimulateFailure(boolean simulateFailure) {
        this.simulateFailure = simulateFailure;
    }
}
