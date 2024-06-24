package com.jomardev25.api;

import java.net.SocketTimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.jomardev25.dto.ShippingDiscountDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ShippingDiscountApi {

    @Value("${voucher-service.url}")
    private String VOUCHER_SERVICE_URL;

    @Value("${voucher-service.api-key}")
    private String VOUCHER_SERVICE_API_KEY;

    private final RestTemplate restTemplate;
   
    @Retry(name = "shippingDiscountServiceRetry", fallbackMethod = "getShippingDiscountFallback")
    @CircuitBreaker(name = "shippingDiscountServiceBreaker", fallbackMethod = "getShippingDiscountFallback")
    public ResponseEntity<ShippingDiscountDTO> get(String voucherCode) {
        var voucherServiceUrl = String.format("%s/%s?key=%s", VOUCHER_SERVICE_URL, voucherCode, VOUCHER_SERVICE_API_KEY);
        System.out.println(voucherServiceUrl);
        var response = restTemplate.getForEntity(voucherServiceUrl, ShippingDiscountDTO.class);
        return response;
    }

    public ResponseEntity<ShippingDiscountDTO> getShippingDiscountFallback(Exception ex) {
        var cause = "Something went wrong.";
        if (ex instanceof ResourceAccessException) {
            if (ex.getCause() instanceof ResourceAccessException) {
                cause = "Connection timeout.";
            }
            if (ex.getCause() instanceof SocketTimeoutException) {
                cause = "Read timeout.";
            }
        } else if (ex instanceof HttpServerErrorException) {
            cause = "Server error.";
        } else if (ex instanceof HttpClientErrorException) {
            cause = "Client error.";
        } else if (ex instanceof CallNotPermittedException) {
            cause = "Open circuit breaker.";
        }
        var message = String.format("%s caused fallback, caught exception: %s", cause, ex.getMessage());
        System.out.println(message);
        return ResponseEntity.ok().body(ShippingDiscountDTO.builder().build());
    }

}
