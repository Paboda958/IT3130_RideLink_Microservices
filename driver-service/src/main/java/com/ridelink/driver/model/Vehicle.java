package com.ridelink.driver.model;

public class Vehicle {

    private String make;
    private String model;
    private String color;
    private String licensePlate;
    private String vehicleType; // SEDAN, SUV, VAN, etc.

    public Vehicle() {}

    public Vehicle(String make, String model, String color, String licensePlate, String vehicleType) {
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
