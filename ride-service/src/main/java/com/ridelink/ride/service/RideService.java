package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.PaymentServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.exception.InvalidStatusTransitionException;
import com.ridelink.ride.exception.NoDriverAvailableException;
import com.ridelink.ride.exception.ResourceNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final SequenceGenerator sequenceGenerator;

    public RideService(RideRepository rideRepository, DriverServiceClient driverServiceClient,
                       PaymentServiceClient paymentServiceClient, SequenceGenerator sequenceGenerator) {
        this.rideRepository = rideRepository;
        this.driverServiceClient = driverServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.sequenceGenerator = sequenceGenerator;
    }

    public RideDto createRide(CreateRideRequest request) {
        Ride ride = new Ride();
        ride.setId(sequenceGenerator.nextId("rides"));
        ride.setRequestedAt(LocalDateTime.now());
        ride.setPassengerId(request.getPassengerId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDestinationLocation(request.getDestinationLocation());

        double dist = request.getEstimatedDistanceKm() != null ? request.getEstimatedDistanceKm() : 5.0;
        ride.setDistanceKm(dist);

        // Fetch fare estimate from Payment Service
        FareEstimateDto fareEstimate = paymentServiceClient.getFareEstimate(
                request.getPickupLocation(), request.getDestinationLocation(), dist
        );
        ride.setEstimatedFare(fareEstimate.getTotalEstimatedFare());
        ride.setStatus(RideStatus.REQUESTED);

        if (request.isAutoAssignDriver()) {
            List<DriverDto> availableDrivers = driverServiceClient.getAvailableDrivers(request.getPickupLocation());
            if (availableDrivers.isEmpty()) {
                throw new NoDriverAvailableException("No eligible drivers currently available near " + request.getPickupLocation());
            }
            DriverDto selectedDriver = availableDrivers.get(0);
            ride.setDriverId(selectedDriver.getId());
            ride.setStatus(RideStatus.ASSIGNED);
            ride.setAssignedAt(LocalDateTime.now());
            driverServiceClient.updateDriverAvailability(selectedDriver.getId(), "BUSY");
        }

        Ride saved = rideRepository.save(ride);
        return mapToDto(saved);
    }

    public RideDto assignDriver(Long rideId, AssignDriverRequest request) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidStatusTransitionException("Cannot assign driver to a ride with status: " + ride.getStatus());
        }

        ride.setDriverId(request.getDriverId());
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setAssignedAt(LocalDateTime.now());

        driverServiceClient.updateDriverAvailability(request.getDriverId(), "BUSY");

        Ride saved = rideRepository.save(ride);
        return mapToDto(saved);
    }

    public RideDto updateStatus(Long rideId, StatusUpdateRequest request) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));

        RideStatus currentStatus = ride.getStatus();
        RideStatus targetStatus = request.getStatus();

        validateTransition(currentStatus, targetStatus);

        ride.setStatus(targetStatus);
        LocalDateTime now = LocalDateTime.now();

        switch (targetStatus) {
            case ACCEPTED:
                ride.setAcceptedAt(now);
                break;
            case IN_PROGRESS:
                ride.setStartedAt(now);
                break;
            case COMPLETED:
                ride.setCompletedAt(now);
                // Calculate final fare rule
                double distance = ride.getDistanceKm() != null ? ride.getDistanceKm() : 5.0;
                double finalFare = 250.0 + (distance * 120.0);
                ride.setFinalFare(Math.round(finalFare * 100.0) / 100.0);
                // Free up driver
                if (ride.getDriverId() != null) {
                    driverServiceClient.updateDriverAvailability(ride.getDriverId(), "AVAILABLE");
                }
                break;
            case CANCELLED:
                ride.setCancelledAt(now);
                ride.setCancellationReason(request.getCancellationReason() != null ? request.getCancellationReason() : "Cancelled by user");
                // Free up driver if assigned
                if (ride.getDriverId() != null) {
                    driverServiceClient.updateDriverAvailability(ride.getDriverId(), "AVAILABLE");
                }
                break;
            default:
                break;
        }

        Ride saved = rideRepository.save(ride);
        return mapToDto(saved);
    }

    public RideDto rateRider(Long rideId, RideRatingRequest request) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));

        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new InvalidStatusTransitionException("Rider can only be rated after the trip is completed.");
        }

        if (request.getRating() != null) {
            ride.setRiderRating(request.getRating());
        }
        if (request.getReview() != null) {
            ride.setRiderReview(request.getReview());
        }

        Ride saved = rideRepository.save(ride);
        return mapToDto(saved);
    }

    public RideDto getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + id));
        return mapToDto(ride);
    }

    public List<RideDto> getRidesByPassenger(Long passengerId) {
        return rideRepository.findByPassengerId(passengerId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RideDto> getRidesByDriver(Long driverId) {
        return rideRepository.findByDriverId(driverId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RideDto> getRidesByStatus(RideStatus status) {
        return rideRepository.findByStatus(status).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RideDto> getAllRides() {
        return rideRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private void validateTransition(RideStatus current, RideStatus target) {
        if (current == target) {
            return;
        }
        if (current == RideStatus.COMPLETED || current == RideStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Cannot change status of a " + current + " ride.");
        }

        boolean valid = false;
        if (current == RideStatus.REQUESTED && (target == RideStatus.ASSIGNED || target == RideStatus.CANCELLED)) {
            valid = true;
        } else if (current == RideStatus.ASSIGNED && (target == RideStatus.ACCEPTED || target == RideStatus.CANCELLED)) {
            valid = true;
        } else if (current == RideStatus.ACCEPTED && (target == RideStatus.IN_PROGRESS || target == RideStatus.CANCELLED)) {
            valid = true;
        } else if (current == RideStatus.IN_PROGRESS && (target == RideStatus.COMPLETED || target == RideStatus.CANCELLED)) {
            valid = true;
        }

        if (!valid) {
            throw new InvalidStatusTransitionException("Invalid ride status transition from " + current + " to " + target);
        }
    }

    private RideDto mapToDto(Ride ride) {
        RideDto dto = new RideDto();
        dto.setId(ride.getId());
        dto.setPassengerId(ride.getPassengerId());
        dto.setDriverId(ride.getDriverId());
        dto.setPickupLocation(ride.getPickupLocation());
        dto.setDestinationLocation(ride.getDestinationLocation());
        dto.setDistanceKm(ride.getDistanceKm());
        dto.setEstimatedFare(ride.getEstimatedFare());
        dto.setFinalFare(ride.getFinalFare());
        dto.setRiderRating(ride.getRiderRating());
        dto.setRiderReview(ride.getRiderReview());
        dto.setStatus(ride.getStatus());
        dto.setCancellationReason(ride.getCancellationReason());
        dto.setRequestedAt(ride.getRequestedAt());
        dto.setAssignedAt(ride.getAssignedAt());
        dto.setAcceptedAt(ride.getAcceptedAt());
        dto.setStartedAt(ride.getStartedAt());
        dto.setCompletedAt(ride.getCompletedAt());
        dto.setCancelledAt(ride.getCancelledAt());
        return dto;
    }
}
