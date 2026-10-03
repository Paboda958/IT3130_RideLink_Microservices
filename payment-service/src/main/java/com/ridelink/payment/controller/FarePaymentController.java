package com.ridelink.payment.controller;

import com.ridelink.payment.dto.*;
import com.ridelink.payment.service.FareCalculationService;
import com.ridelink.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Fare & Payment Operations", description = "Endpoints for fare estimates, payment recording, and receipt generation")
public class FarePaymentController {

    private final FareCalculationService fareCalculationService;
    private final PaymentService paymentService;

    public FarePaymentController(FareCalculationService fareCalculationService, PaymentService paymentService) {
        this.fareCalculationService = fareCalculationService;
        this.paymentService = paymentService;
    }

    @PostMapping("/fare-estimate")
    @Operation(summary = "Calculate fare estimate for pickup and destination")
    public ResponseEntity<FareEstimateResponse> getFareEstimate(@Valid @RequestBody FareEstimateRequest request) {
        return ResponseEntity.ok(fareCalculationService.calculateEstimate(request));
    }

    @PostMapping("/process")
    @Operation(summary = "Record and process a simulated payment for a completed ride")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentProcessRequest request) {
        PaymentResponse response = paymentService.processSimulatedPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/ride/{rideId}")
    @Operation(summary = "Retrieve payment record by Ride ID")
    public ResponseEntity<PaymentResponse> getPaymentByRideId(@PathVariable Long rideId) {
        return ResponseEntity.ok(paymentService.getPaymentByRideId(rideId));
    }

    @GetMapping("/receipt/ride/{rideId}")
    @Operation(summary = "Generate and retrieve receipt/payment record by Ride ID")
    public ResponseEntity<ReceiptDto> getReceiptByRideId(@PathVariable Long rideId) {
        return ResponseEntity.ok(paymentService.getReceiptByRideId(rideId));
    }
}
