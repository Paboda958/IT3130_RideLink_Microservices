package com.ridelink.ride.client;

import com.ridelink.ride.dto.FareEstimateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public PaymentServiceClient(RestTemplate restTemplate, @Value("${services.payment-service.url}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public FareEstimateDto getFareEstimate(String pickup, String destination, Double distanceKm) {
        try {
            String url = paymentServiceUrl + "/api/v1/payments/fare-estimate";
            Map<String, Object> request = Map.of(
                    "pickupLocation", pickup,
                    "destinationLocation", destination,
                    "estimatedDistanceKm", distanceKm != null ? distanceKm : 5.0
            );
            return restTemplate.postForObject(url, request, FareEstimateDto.class);
        } catch (Exception ex) {
            // Fallback estimation calculation if Payment Service is temporarily unreachable
            FareEstimateDto fallback = new FareEstimateDto();
            fallback.setPickupLocation(pickup);
            fallback.setDestinationLocation(destination);
            fallback.setDistanceKm(distanceKm != null ? distanceKm : 5.0);
            fallback.setTotalEstimatedFare(250.0 + (fallback.getDistanceKm() * 120.0));
            return fallback;
        }
    }
}
