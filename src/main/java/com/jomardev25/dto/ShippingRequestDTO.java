package com.jomardev25.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShippingRequestDTO {
    
    @NotNull
    @Min(value = 1, message = "Weight must be greater than or equal to 1kg")
    @Max(value = 50, message = "Weight must be less than or equal to 50kg")
    private double weight;

    @NotNull
    @Min(1)
    private double height;

    @NotNull
    @Min(1)
    private double width;

    @NotNull
    @Min(1)
    private double length;

    private String voucher;

}
