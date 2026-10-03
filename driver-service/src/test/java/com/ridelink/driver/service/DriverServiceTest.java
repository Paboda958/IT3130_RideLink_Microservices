package com.ridelink.driver.service;

import com.ridelink.driver.dto.AvailabilityUpdateRequest;
import com.ridelink.driver.dto.DriverProfileDto;
import com.ridelink.driver.dto.DriverRegisterRequest;
import com.ridelink.driver.dto.VehicleDto;
import com.ridelink.driver.exception.InvalidOperationException;
import com.ridelink.driver.exception.ResourceNotFoundException;
import com.ridelink.driver.model.DriverAvailability;
import com.ridelink.driver.model.DriverProfile;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @InjectMocks
    private DriverService driverService;

    private DriverRegisterRequest registerRequest;
    private DriverProfile driverProfile;

    @BeforeEach
    void setUp() {
        VehicleDto vehicleDto = new VehicleDto("Toyota", "Prius", "Silver", "CAB-1234", "SEDAN");
        registerRequest = new DriverRegisterRequest(10L, "DL987654321", "Colombo", vehicleDto);

        Vehicle vehicle = new Vehicle("Toyota", "Prius", "Silver", "CAB-1234", "SEDAN");
        driverProfile = new DriverProfile(1L, 10L, "DL987654321", 5.0, DriverAvailability.AVAILABLE, vehicle);
        driverProfile.setServiceArea("Colombo");
    }

    @Test
    void registerDriverProfile_Success() {
        when(sequenceGenerator.nextId("driver_profiles")).thenReturn(1L);
        when(driverRepository.existsByUserAccountId(10L)).thenReturn(false);
        when(driverRepository.existsByDriverLicenseNumber("DL987654321")).thenReturn(false);
        when(driverRepository.save(any(DriverProfile.class))).thenReturn(driverProfile);

        DriverProfileDto result = driverService.registerDriverProfile(registerRequest);

        assertNotNull(result);
        assertEquals(10L, result.getUserAccountId());
        assertEquals("DL987654321", result.getDriverLicenseNumber());
    }

    @Test
    void registerDriverProfile_DuplicateUserAccountId_ThrowsInvalidOperationException() {
        when(driverRepository.existsByUserAccountId(10L)).thenReturn(true);

        assertThrows(InvalidOperationException.class, () -> driverService.registerDriverProfile(registerRequest));
    }

    @Test
    void getEligibleAvailableDrivers_Success() {
        when(driverRepository.findByAvailabilityStatusAndServiceAreaContainingIgnoreCase(DriverAvailability.AVAILABLE, "Colombo"))
                .thenReturn(List.of(driverProfile));

        List<DriverProfileDto> result = driverService.getEligibleAvailableDrivers("Colombo");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(DriverAvailability.AVAILABLE, result.get(0).getAvailabilityStatus());
    }

    @Test
    void updateAvailability_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverProfile));
        when(driverRepository.save(any(DriverProfile.class))).thenReturn(driverProfile);

        AvailabilityUpdateRequest request = new AvailabilityUpdateRequest(DriverAvailability.BUSY);
        DriverProfileDto result = driverService.updateAvailability(1L, request);

        assertNotNull(result);
        verify(driverRepository, times(1)).save(driverProfile);
    }
}
