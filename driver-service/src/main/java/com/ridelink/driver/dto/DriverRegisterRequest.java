package com.ridelink.driver.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DriverRegisterRequest {

    @NotNull(message = "User Account ID is required")
    private Long userAccountId;

    @NotBlank(message = "Driver License Number is required")
    private String driverLicenseNumber;

    private String serviceArea;

    @Valid
    @NotNull(message = "Vehicle information is required")
    private VehicleDto vehicle;

    public DriverRegisterRequest() {}

    public DriverRegisterRequest(Long userAccountId, String driverLicenseNumber, String serviceArea, VehicleDto vehicle) {
        this.userAccountId = userAccountId;
        this.driverLicenseNumber = driverLicenseNumber;
        this.serviceArea = serviceArea;
        this.vehicle = vehicle;
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
