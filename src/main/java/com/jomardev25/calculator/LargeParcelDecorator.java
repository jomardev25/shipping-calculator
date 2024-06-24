package com.jomardev25.calculator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LargeParcelDecorator implements ShippingCostCalculator {

    private final double volumetricWeight;
    private final double rate;

    @Override
    public double calculateCost() {
        return rate * volumetricWeight;
    }

}
