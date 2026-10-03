package com.ridelink.payment.repository;

import com.ridelink.payment.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, Long> {
    Optional<Payment> findByRideId(Long rideId);
    List<Payment> findByPassengerId(Long passengerId);
    List<Payment> findByDriverId(Long driverId);
}
