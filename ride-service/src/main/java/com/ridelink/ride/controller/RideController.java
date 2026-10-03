package com.ridelink.ride.controller;

import com.ridelink.ride.dto.*;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@Tag(name = "Ride Management", description = "Endpoints for ride requests, driver assignment, ride lifecycle transitions, and ride lookup")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    @Operation(summary = "Create a new ride request")
    public ResponseEntity<RideDto> createRide(@Valid @RequestBody CreateRideRequest request) {
        RideDto created = rideService.createRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign a driver to a requested ride")
    public ResponseEntity<RideDto> assignDriver(@PathVariable Long id, @Valid @RequestBody AssignDriverRequest request) {
        return ResponseEntity.ok(rideService.assignDriver(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update ride status lifecycle (ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED)")
    public ResponseEntity<RideDto> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(rideService.updateStatus(id, request));
    }

    @PostMapping("/{id}/rate-rider")
    @Operation(summary = "Rate the rider after a completed trip")
    public ResponseEntity<RideDto> rateRider(@PathVariable Long id, @Valid @RequestBody RideRatingRequest request) {
        return ResponseEntity.ok(rideService.rateRider(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride details by Ride ID")
    public ResponseEntity<RideDto> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "Get all rides for a specific passenger")
    public ResponseEntity<List<RideDto>> getRidesByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(rideService.getRidesByPassenger(passengerId));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get all rides assigned to a specific driver")
    public ResponseEntity<List<RideDto>> getRidesByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(rideService.getRidesByDriver(driverId));
    }

    @GetMapping
    @Operation(summary = "Get all rides (Optional filter by status)")
    public ResponseEntity<List<RideDto>> getAllRides(@RequestParam(required = false) RideStatus status) {
        if (status != null) {
            return ResponseEntity.ok(rideService.getRidesByStatus(status));
        }
        return ResponseEntity.ok(rideService.getAllRides());
    }
}
