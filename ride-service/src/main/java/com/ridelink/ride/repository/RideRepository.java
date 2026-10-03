package com.ridelink.ride.repository;

import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends MongoRepository<Ride, Long> {
    List<Ride> findByPassengerId(Long passengerId);
    List<Ride> findByDriverId(Long driverId);
    List<Ride> findByStatus(RideStatus status);
}
