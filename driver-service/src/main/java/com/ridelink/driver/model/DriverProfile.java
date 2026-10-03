package com.ridelink.driver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "driver_profiles")
public class DriverProfile {

    @Id
    private Long id;

    @Indexed(unique = true)
    private Long userAccountId;

    @Indexed(unique = true)
    private String driverLicenseNumber;

    private Double rating;

    private DriverAvailability availabilityStatus;

    private Double currentLatitude;
    private Double currentLongitude;
    private String serviceArea;

    private Vehicle vehicle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DriverProfile() {}

    public DriverProfile(Long id, Long userAccountId, String driverLicenseNumber, Double rating, DriverAvailability availabilityStatus, Vehicle vehicle) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.driverLicenseNumber = driverLicenseNumber;
        this.rating = rating;
        this.availabilityStatus = availabilityStatus;
        this.vehicle = vehicle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public DriverAvailability getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(DriverAvailability availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
