package com.ridelink.payment.service;

import com.ridelink.payment.dto.FareEstimateRequest;
import com.ridelink.payment.dto.FareEstimateResponse;
import org.springframework.stereotype.Service;

@Service
public class FareCalculationService {

    public static final double BASE_FARE = 250.0;
    public static final double RATE_PER_KM = 120.0;
    public static final double PEAK_HOUR_SURGE_MULTIPLIER = 1.3;

    public FareEstimateResponse calculateEstimate(FareEstimateRequest request) {
        double distanceKm = request.getEstimatedDistanceKm() != null ? request.getEstimatedDistanceKm() : 5.0;
        double baseTotal = BASE_FARE + (distanceKm * RATE_PER_KM);

        boolean peakHour = Boolean.TRUE.equals(request.getPeakHour()) || isPeakHourNow();
        double surgeMultiplier = peakHour ? PEAK_HOUR_SURGE_MULTIPLIER : 1.0;
        double surgedTotal = baseTotal * surgeMultiplier;
        double couponDiscount = calculateCouponDiscount(surgedTotal, request.getCouponCode());
        double totalFare = Math.round((surgedTotal - couponDiscount) * 100.0) / 100.0;

        FareEstimateResponse response = new FareEstimateResponse(
                request.getPickupLocation(),
                request.getDestinationLocation(),
                distanceKm,
                BASE_FARE,
                RATE_PER_KM,
                totalFare
        );
        response.setSurgeMultiplier(surgeMultiplier);
        response.setCouponDiscount(Math.round(couponDiscount * 100.0) / 100.0);
        return response;
    }

    public double calculateFinalFare(double distanceKm) {
        double dist = distanceKm > 0 ? distanceKm : 1.0;
        double total = BASE_FARE + (dist * RATE_PER_KM);
        return Math.round(total * 100.0) / 100.0;
    }

    public double calculateProcessedAmount(Double baseAmount, Double distanceKm, String couponCode, Boolean peakHour) {
        double rawAmount = baseAmount != null ? baseAmount : calculateFinalFare(distanceKm);
        double multiplier = Boolean.TRUE.equals(peakHour) || isPeakHourNow() ? PEAK_HOUR_SURGE_MULTIPLIER : 1.0;
        double surged = rawAmount * multiplier;
        double discount = calculateCouponDiscount(surged, couponCode);
        return Math.round((surged - discount) * 100.0) / 100.0;
    }

    private boolean isPeakHourNow() {
        int hour = java.time.LocalTime.now().getHour();
        return hour >= 17 && hour <= 22;
    }

    private double calculateCouponDiscount(double amount, String couponCode) {
        if (couponCode == null || couponCode.isBlank()) {
            return 0.0;
        }

        String normalized = couponCode.trim().toUpperCase();
        if ("SAVE20".equals(normalized)) {
            return Math.round((amount * 0.20) * 100.0) / 100.0;
        }
        if ("WELCOME10".equals(normalized) || "RIDE10".equals(normalized)) {
            return Math.round((amount * 0.10) * 100.0) / 100.0;
        }
        return 0.0;
    }
}
