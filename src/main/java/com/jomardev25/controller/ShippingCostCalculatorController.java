package com.jomardev25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jomardev25.dto.ErrorResponseDTO;
import com.jomardev25.dto.ShippingRequestDTO;
import com.jomardev25.dto.ShippingResponseDTO;
import com.jomardev25.service.ShippingCalculatorService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Shipping Cost Calculator", description = "API that will calculate the cost of delivery of a parcel based on weight and volume")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shipping")
public class ShippingCostCalculatorController {

    private final ShippingCalculatorService shippingCalculatorService;

    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ShippingResponseDTO.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = ErrorResponseDTO.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = ErrorResponseDTO.class)) }) })
    @PostMapping
    public ResponseEntity<ShippingResponseDTO> getShippingCost(@Valid @RequestBody ShippingRequestDTO request) {
        var shippingCost = shippingCalculatorService.calculateShippingCost(request.getWeight(), request.getHeight(),
                request.getWidth(), request.getLength(), request.getVoucher());
        var response = ShippingResponseDTO.builder().cost(shippingCost).build();
        return ResponseEntity.ok().body(response);
    }

}
