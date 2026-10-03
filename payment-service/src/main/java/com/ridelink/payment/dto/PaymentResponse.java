package com.ridelink.payment.dto;

import com.ridelink.payment.model.PaymentMethod;
import com.ridelink.payment.model.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponse {

    private Long paymentId;
    private Long rideId;
    private Long passengerId;
    private Long driverId;
    private Double amount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionRef;
    private String failureReason;
    private LocalDateTime paymentTime;

    public PaymentResponse() {}

    public PaymentResponse(Long paymentId, Long rideId, Long passengerId, Long driverId, Double amount,
                           PaymentStatus paymentStatus, PaymentMethod paymentMethod, String transactionRef,
                           String failureReason, LocalDateTime paymentTime) {
        this.paymentId = paymentId;
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionRef = transactionRef;
        this.failureReason = failureReason;
        this.paymentTime = paymentTime;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}
