package com.ridelink.driver.dto;

import com.ridelink.driver.model.DriverAvailability;

public class DriverProfileDto {

    private Long id;
    private Long userAccountId;
    private String driverLicenseNumber;
    private Double rating;
    private DriverAvailability availabilityStatus;
    private Double currentLatitude;
    private Double currentLongitude;
    private String serviceArea;
    private VehicleDto vehicle;

    public DriverProfileDto() {}

    public DriverProfileDto(Long id, Long userAccountId, String driverLicenseNumber, Double rating,
                            DriverAvailability availabilityStatus, Double currentLatitude, Double currentLongitude,
                            String serviceArea, VehicleDto vehicle) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.driverLicenseNumber = driverLicenseNumber;
        this.rating = rating;
        this.availabilityStatus = availabilityStatus;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.serviceArea = serviceArea;
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

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }
}
