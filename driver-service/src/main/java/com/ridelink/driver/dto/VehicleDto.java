package com.ridelink.driver.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleDto {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    public VehicleDto() {}

    public VehicleDto(String make, String model, String color, String licensePlate, String vehicleType) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
