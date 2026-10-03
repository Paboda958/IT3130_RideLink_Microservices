package com.ridelink.payment.service;

import com.ridelink.payment.dto.FareEstimateRequest;
import com.ridelink.payment.dto.FareEstimateResponse;
import com.ridelink.payment.dto.PaymentProcessRequest;
import com.ridelink.payment.dto.PaymentResponse;
import com.ridelink.payment.exception.PaymentFailedException;
import com.ridelink.payment.model.Payment;
import com.ridelink.payment.model.PaymentMethod;
import com.ridelink.payment.model.PaymentStatus;
import com.ridelink.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private FareCalculationService fareCalculationService;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentProcessRequest processRequest;
    private Payment payment;

    @BeforeEach
    void setUp() {
        processRequest = new PaymentProcessRequest(100L, 5L, 20L, 850.0, 5.0, PaymentMethod.CREDIT_CARD);
        payment = new Payment();
        payment.setId(1L);
        payment.setRideId(100L);
        payment.setPassengerId(5L);
        payment.setDriverId(20L);
        payment.setAmount(850.0);
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionRef("TXN-TEST1234");
    }

    @Test
    void processSimulatedPayment_Success() {
        when(sequenceGenerator.nextId("payments")).thenReturn(1L);
        when(paymentRepository.findByRideId(100L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.processSimulatedPayment(processRequest);

        assertNotNull(response);
        assertEquals(100L, response.getRideId());
        assertEquals(PaymentStatus.SUCCESS, response.getPaymentStatus());
    }

    @Test
    void processSimulatedPayment_SimulatedFailure_ThrowsPaymentFailedException() {
        processRequest.setSimulateFailure(true);
        when(sequenceGenerator.nextId("payments")).thenReturn(1L);
        when(paymentRepository.findByRideId(100L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        assertThrows(PaymentFailedException.class, () -> paymentService.processSimulatedPayment(processRequest));
    }

    @Test
    void calculateEstimate_Success() {
        FareCalculationService calculationService = new FareCalculationService();
        FareEstimateRequest request = new FareEstimateRequest("Colombo 03", "Nugegoda", 10.0);

        FareEstimateResponse response = calculationService.calculateEstimate(request);

        assertNotNull(response);
        assertEquals(1450.0, response.getTotalEstimatedFare()); // 250 + (10 * 120) = 1450
    }

    @Test
    void calculateEstimate_WithPeakHourAndCoupon_AppliesSurgeAndDiscount() {
        FareCalculationService calculationService = new FareCalculationService();
        FareEstimateRequest request = new FareEstimateRequest("Colombo 03", "Nugegoda", 10.0);
        request.setPeakHour(true);
        request.setCouponCode("SAVE20");

        FareEstimateResponse response = calculationService.calculateEstimate(request);

        assertNotNull(response);
        assertEquals(1508.0, response.getTotalEstimatedFare());
        assertEquals(1.3, response.getSurgeMultiplier());
        assertEquals(377.0, response.getCouponDiscount());
    }
}
