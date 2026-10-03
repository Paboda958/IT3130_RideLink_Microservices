package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.PaymentServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.exception.InvalidStatusTransitionException;
import com.ridelink.ride.exception.NoDriverAvailableException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverServiceClient;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @InjectMocks
    private RideService rideService;

    private CreateRideRequest createRideRequest;
    private Ride ride;

    @BeforeEach
    void setUp() {
        createRideRequest = new CreateRideRequest(1L, "Colombo Fort", "Bambalapitiya", 4.0);
        ride = new Ride();
        ride.setId(10L);
        ride.setPassengerId(1L);
        ride.setPickupLocation("Colombo Fort");
        ride.setDestinationLocation("Bambalapitiya");
        ride.setDistanceKm(4.0);
        ride.setEstimatedFare(730.0);
        ride.setStatus(RideStatus.REQUESTED);
    }

    @Test
    void createRide_Success() {
        when(sequenceGenerator.nextId("rides")).thenReturn(1L);
        FareEstimateDto fareEstimate = new FareEstimateDto();
        fareEstimate.setTotalEstimatedFare(730.0);

        when(paymentServiceClient.getFareEstimate(anyString(), anyString(), anyDouble())).thenReturn(fareEstimate);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        RideDto result = rideService.createRide(createRideRequest);

        assertNotNull(result);
        assertEquals("Colombo Fort", result.getPickupLocation());
        assertEquals(RideStatus.REQUESTED, result.getStatus());
        assertEquals(730.0, result.getEstimatedFare());
    }

    @Test
    void createRide_AutoAssign_NoDriver_ThrowsNoDriverAvailableException() {
        createRideRequest.setAutoAssignDriver(true);
        FareEstimateDto fareEstimate = new FareEstimateDto();
        fareEstimate.setTotalEstimatedFare(730.0);

        when(paymentServiceClient.getFareEstimate(anyString(), anyString(), anyDouble())).thenReturn(fareEstimate);
        when(driverServiceClient.getAvailableDrivers(anyString())).thenReturn(Collections.emptyList());

        assertThrows(NoDriverAvailableException.class, () -> rideService.createRide(createRideRequest));
    }

    @Test
    void updateStatus_InvalidTransition_ThrowsInvalidStatusTransitionException() {
        ride.setStatus(RideStatus.COMPLETED);
        when(rideRepository.findById(10L)).thenReturn(Optional.of(ride));

        StatusUpdateRequest request = new StatusUpdateRequest(RideStatus.IN_PROGRESS);

        assertThrows(InvalidStatusTransitionException.class, () -> rideService.updateStatus(10L, request));
    }

    @Test
    void updateStatus_Completed_CalculatesFinalFareAndFreesDriver() {
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setDriverId(20L);
        when(rideRepository.findById(10L)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        StatusUpdateRequest request = new StatusUpdateRequest(RideStatus.COMPLETED);
        RideDto result = rideService.updateStatus(10L, request);

        assertNotNull(result);
        assertEquals(RideStatus.COMPLETED, result.getStatus());
        assertEquals(730.0, result.getFinalFare()); // 250 + (4 * 120) = 730
        verify(driverServiceClient, times(1)).updateDriverAvailability(20L, "AVAILABLE");
    }

    @Test
    void rateRider_CompletedRide_SavesRatingAndReview() {
        ride.setStatus(RideStatus.COMPLETED);
        RideRatingRequest request = new RideRatingRequest();
        request.setRideId(10L);
        request.setRating(4.5);
        request.setReview("Helpful and polite driver");

        when(rideRepository.findById(10L)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        RideDto result = rideService.rateRider(10L, request);

        assertNotNull(result);
        assertEquals(4.5, result.getRiderRating());
        assertEquals("Helpful and polite driver", result.getRiderReview());
    }
}
