package com.ridelink.driver.service;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.exception.InvalidOperationException;
import com.ridelink.driver.exception.ResourceNotFoundException;
import com.ridelink.driver.model.DriverAvailability;
import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final SequenceGenerator sequenceGenerator;

    public DriverService(DriverRepository driverRepository, SequenceGenerator sequenceGenerator) {
        this.driverRepository = driverRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    public DriverProfileDto registerDriverProfile(DriverRegisterRequest request) {
        if (driverRepository.existsByUserAccountId(request.getUserAccountId())) {
            throw new InvalidOperationException("Driver profile already exists for user account ID: " + request.getUserAccountId());
        }
        if (driverRepository.existsByDriverLicenseNumber(request.getDriverLicenseNumber())) {
            throw new InvalidOperationException("Driver license number already registered!");
        }

        DriverProfile profile = new DriverProfile();
        profile.setId(sequenceGenerator.nextId("driver_profiles"));
        profile.setUserAccountId(request.getUserAccountId());
        profile.setDriverLicenseNumber(request.getDriverLicenseNumber());
        profile.setServiceArea(request.getServiceArea());
        profile.setAvailabilityStatus(DriverAvailability.OFF_DUTY);
        profile.setRating(5.0);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(profile.getCreatedAt());

        if (request.getVehicle() != null) {
            Vehicle vehicle = new Vehicle(
                    request.getVehicle().getMake(),
                    request.getVehicle().getModel(),
                    request.getVehicle().getColor(),
                    request.getVehicle().getLicensePlate(),
                    request.getVehicle().getVehicleType()
            );
            profile.setVehicle(vehicle);
        }

        DriverProfile saved = driverRepository.save(profile);
        return mapToDto(saved);
    }

    public DriverProfileDto getDriverById(Long id) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with id: " + id));
        return mapToDto(profile);
    }

    public DriverProfileDto getDriverByUserAccountId(Long userAccountId) {
        DriverProfile profile = driverRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found for user account ID: " + userAccountId));
        return mapToDto(profile);
    }

    public List<DriverProfileDto> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DriverProfileDto> getEligibleAvailableDrivers(String serviceArea) {
        if (serviceArea != null && !serviceArea.isBlank()) {
            return driverRepository.findByAvailabilityStatusAndServiceAreaContainingIgnoreCase(
                    DriverAvailability.AVAILABLE, serviceArea
            ).stream().map(this::mapToDto).collect(Collectors.toList());
        }
        return driverRepository.findByAvailabilityStatus(DriverAvailability.AVAILABLE)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public DriverProfileDto updateAvailability(Long id, AvailabilityUpdateRequest request) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with id: " + id));

        profile.setAvailabilityStatus(request.getAvailabilityStatus());
        profile.setUpdatedAt(LocalDateTime.now());
        DriverProfile updated = driverRepository.save(profile);
        return mapToDto(updated);
    }

    public DriverProfileDto updateLocation(Long id, LocationUpdateRequest request) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with id: " + id));

        profile.setCurrentLatitude(request.getLatitude());
        profile.setCurrentLongitude(request.getLongitude());
        if (request.getServiceArea() != null) {
            profile.setServiceArea(request.getServiceArea());
        }
        profile.setUpdatedAt(LocalDateTime.now());

        DriverProfile updated = driverRepository.save(profile);
        return mapToDto(updated);
    }

    public DriverProfileDto updateVehicle(Long id, VehicleDto vehicleDto) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with id: " + id));

        Vehicle vehicle = new Vehicle(
                vehicleDto.getMake(),
                vehicleDto.getModel(),
                vehicleDto.getColor(),
                vehicleDto.getLicensePlate(),
                vehicleDto.getVehicleType()
        );
        profile.setVehicle(vehicle);
        profile.setUpdatedAt(LocalDateTime.now());

        DriverProfile updated = driverRepository.save(profile);
        return mapToDto(updated);
    }

    private DriverProfileDto mapToDto(DriverProfile profile) {
        VehicleDto vehicleDto = null;
        if (profile.getVehicle() != null) {
            vehicleDto = new VehicleDto(
                    profile.getVehicle().getMake(),
                    profile.getVehicle().getModel(),
                    profile.getVehicle().getColor(),
                    profile.getVehicle().getLicensePlate(),
                    profile.getVehicle().getVehicleType()
            );
        }

        return new DriverProfileDto(
                profile.getId(),
                profile.getUserAccountId(),
                profile.getDriverLicenseNumber(),
                profile.getRating(),
                profile.getAvailabilityStatus(),
                profile.getCurrentLatitude(),
                profile.getCurrentLongitude(),
                profile.getServiceArea(),
                vehicleDto
        );
    }
}
