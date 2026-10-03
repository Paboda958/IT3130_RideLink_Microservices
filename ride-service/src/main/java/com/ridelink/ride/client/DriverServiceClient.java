package com.ridelink.ride.client;

import com.ridelink.ride.dto.DriverDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DriverServiceClient {

    private final RestTemplate restTemplate;
    private final String driverServiceUrl;

    public DriverServiceClient(RestTemplate restTemplate, @Value("${services.driver-service.url}") String driverServiceUrl) {
        this.restTemplate = restTemplate;
        this.driverServiceUrl = driverServiceUrl;
    }

    public List<DriverDto> getAvailableDrivers(String serviceArea) {
        try {
            String url = driverServiceUrl + "/api/v1/drivers/available";
            if (serviceArea != null && !serviceArea.isBlank()) {
                url += "?serviceArea=" + serviceArea;
            }
            ResponseEntity<List<DriverDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<List<DriverDto>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public boolean updateDriverAvailability(Long driverId, String status) {
        try {
            String url = driverServiceUrl + "/api/v1/drivers/" + driverId + "/availability";
            restTemplate.patchForObject(url, Map.of("availabilityStatus", status), Object.class);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
