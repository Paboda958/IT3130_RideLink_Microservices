package com.ridelink.driver.controller;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Driver & Vehicle Operations", description = "Endpoints for driver profile, vehicle details, availability, and location management")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Register driver operational profile and vehicle details")
    public ResponseEntity<DriverProfileDto> registerDriver(@Valid @RequestBody DriverRegisterRequest request) {
        DriverProfileDto created = driverService.registerDriverProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get driver profile by Driver ID")
    public ResponseEntity<DriverProfileDto> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping("/user-account/{userAccountId}")
    @Operation(summary = "Get driver profile by User Account ID")
    public ResponseEntity<DriverProfileDto> getDriverByUserAccountId(@PathVariable Long userAccountId) {
        return ResponseEntity.ok(driverService.getDriverByUserAccountId(userAccountId));
    }

    @GetMapping
    @Operation(summary = "Get all drivers")
    public ResponseEntity<List<DriverProfileDto>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/available")
    @Operation(summary = "Retrieve eligible available drivers (optional service area filter)")
    public ResponseEntity<List<DriverProfileDto>> getEligibleAvailableDrivers(@RequestParam(name = "serviceArea", required = false) String serviceArea) {
        return ResponseEntity.ok(driverService.getEligibleAvailableDrivers(serviceArea));
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update driver availability status (AVAILABLE, OFF_DUTY, BUSY)")
    public ResponseEntity<DriverProfileDto> updateAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityUpdateRequest request) {
        return ResponseEntity.ok(driverService.updateAvailability(id, request));
    }

    @PatchMapping("/{id}/location")
    @Operation(summary = "Update driver simulated location and service area")
    public ResponseEntity<DriverProfileDto> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationUpdateRequest request) {
        return ResponseEntity.ok(driverService.updateLocation(id, request));
    }

    @PutMapping("/{id}/vehicle")
    @Operation(summary = "Update driver vehicle details")
    public ResponseEntity<DriverProfileDto> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(driverService.updateVehicle(id, vehicleDto));
    }
}
