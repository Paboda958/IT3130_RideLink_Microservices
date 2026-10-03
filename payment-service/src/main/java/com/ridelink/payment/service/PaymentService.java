package com.ridelink.payment.service;

import com.ridelink.payment.dto.PaymentProcessRequest;
import com.ridelink.payment.dto.PaymentResponse;
import com.ridelink.payment.dto.ReceiptDto;
import com.ridelink.payment.exception.PaymentFailedException;
import com.ridelink.payment.exception.ResourceNotFoundException;
import com.ridelink.payment.model.Payment;
import com.ridelink.payment.model.PaymentStatus;
import com.ridelink.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FareCalculationService fareCalculationService;
    private final SequenceGenerator sequenceGenerator;

    public PaymentService(PaymentRepository paymentRepository, FareCalculationService fareCalculationService,
                          SequenceGenerator sequenceGenerator) {
        this.paymentRepository = paymentRepository;
        this.fareCalculationService = fareCalculationService;
        this.sequenceGenerator = sequenceGenerator;
    }

    public PaymentResponse processSimulatedPayment(PaymentProcessRequest request) {
        paymentRepository.findByRideId(request.getRideId()).ifPresent(p -> {
            if (p.getPaymentStatus() == PaymentStatus.SUCCESS) {
                throw new PaymentFailedException("Payment already processed for ride ID: " + request.getRideId());
            }
        });

        Payment payment = new Payment();
        payment.setId(sequenceGenerator.nextId("payments"));
        payment.setCreatedAt(LocalDateTime.now());
        payment.setRideId(request.getRideId());
        payment.setPassengerId(request.getPassengerId());
        payment.setDriverId(request.getDriverId());

        double adjustedAmount = fareCalculationService.calculateProcessedAmount(
                request.getAmount(), request.getDistanceKm(), request.getCouponCode(), request.getPeakHour());
        payment.setAmount(adjustedAmount);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setBaseFare(FareCalculationService.BASE_FARE);

        double dist = request.getDistanceKm() != null ? request.getDistanceKm() : 1.0;
        payment.setDistanceKm(dist);
        payment.setDistanceFare(dist * FareCalculationService.RATE_PER_KM);

        if (request.isSimulateFailure()) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Simulated card decline or insufficient funds");
            Payment savedFailed = paymentRepository.save(payment);
            throw new PaymentFailedException("Simulated payment failed: Insufficient funds / Declined transaction");
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaymentTime(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    public PaymentResponse getPaymentByRideId(Long rideId) {
        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found for ride ID: " + rideId));
        return mapToResponse(payment);
    }

    public ReceiptDto getReceiptByRideId(Long rideId) {
        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found for ride ID: " + rideId));

        ReceiptDto receipt = new ReceiptDto();
        receipt.setReceiptNumber("REC-" + payment.getRideId() + "-" + payment.getId());
        receipt.setPaymentId(payment.getId());
        receipt.setRideId(payment.getRideId());
        receipt.setPassengerId(payment.getPassengerId());
        receipt.setDriverId(payment.getDriverId());
        receipt.setBaseFare(payment.getBaseFare());
        receipt.setDistanceKm(payment.getDistanceKm());
        receipt.setDistanceFare(payment.getDistanceFare());
        receipt.setTotalAmount(payment.getAmount());
        receipt.setStatus(payment.getPaymentStatus());
        receipt.setPaymentMethod(payment.getPaymentMethod());
        receipt.setTransactionRef(payment.getTransactionRef());
        receipt.setIssuedAt(payment.getPaymentTime() != null ? payment.getPaymentTime() : payment.getCreatedAt());

        return receipt;
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getRideId(),
                payment.getPassengerId(),
                payment.getDriverId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaymentMethod(),
                payment.getTransactionRef(),
                payment.getFailureReason(),
                payment.getPaymentTime()
        );
    }
}
