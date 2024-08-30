package com.jomardev25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jomardev25.dto.ShippingRequestDTO;
import com.jomardev25.dto.ShippingResponseDTO;
import com.jomardev25.service.ShippingCalculatorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shipping")
public class ShippingCostCalculatorController {

    private final ShippingCalculatorService shippingCalculatorService;


    @PostMapping
    public ResponseEntity<ShippingResponseDTO> getShippingCost(@Valid @RequestBody ShippingRequestDTO request) {
        var shippingCost = shippingCalculatorService.calculateShippingCost(request.getWeight(), request.getHeight(),
                request.getWidth(), request.getLength(), request.getVoucher());
        var response = ShippingResponseDTO.builder().cost(shippingCost).build();
        return ResponseEntity.ok().body(response);
    }

}
