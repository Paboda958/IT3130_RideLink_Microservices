package com.ridelink.driver.repository;

import com.ridelink.driver.model.DriverAvailability;
import com.ridelink.driver.model.DriverProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends MongoRepository<DriverProfile, Long> {
    Optional<DriverProfile> findByUserAccountId(Long userAccountId);
    boolean existsByUserAccountId(Long userAccountId);
    boolean existsByDriverLicenseNumber(String driverLicenseNumber);
    List<DriverProfile> findByAvailabilityStatus(DriverAvailability availabilityStatus);
    List<DriverProfile> findByAvailabilityStatusAndServiceAreaContainingIgnoreCase(DriverAvailability status, String serviceArea);
}
