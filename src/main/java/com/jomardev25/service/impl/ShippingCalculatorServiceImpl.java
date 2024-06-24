package com.jomardev25.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import com.jomardev25.calculator.HeavyParcelDecorator;
import com.jomardev25.calculator.LargeParcelDecorator;
import com.jomardev25.calculator.MediumParcelDecorator;
import com.jomardev25.calculator.ShippingCostCalculator;
import com.jomardev25.calculator.SmallParcelDecorator;
import com.jomardev25.entity.ShippingRule;
import com.jomardev25.exception.ResourceNotFoundException;
import com.jomardev25.exception.ShippingRuleException;
import com.jomardev25.repository.ShippingRateRepository;
import com.jomardev25.service.ShippingCalculatorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingCalculatorServiceImpl implements ShippingCalculatorService {

    private final ShippingRateRepository shippingRateRepository;
    private final ShippingDiscountServiceImpl shippingDiscountService;

    public BigDecimal calculateShippingCost(double weight, double height, double width, double length, String voucherCode)
            throws RuntimeException {

        var shippingCost = 0.0;
        var volume = height * width * length; // volume
        var rate = 0.0;
        ShippingCostCalculator shippingCalculator; // Large Parcel: Volume is greater than 2500 cm3

        if (weight > 50)
            throw new ShippingRuleException(); // Reject: Weight exceeds 50kg

        if (weight > 10) {
            rate = getShippingRate(ShippingRule.HEAVY_PARCEL);
            shippingCalculator = new HeavyParcelDecorator(weight, rate); // Heavy Parcel: Weight exceeds 10kg
        } else if (volume < 1500) {
            rate = getShippingRate(ShippingRule.SMALL_PARCEL);
            shippingCalculator = new SmallParcelDecorator(volume, rate); // Small Parcel: Volume is less than 1500 cm3
        } else if (volume >= 1500 && volume < 2500) {
            rate = getShippingRate(ShippingRule.MEDIUM_PARCEL);
            shippingCalculator = new MediumParcelDecorator(volume, rate); // Medium Parcel: Volume is less than 2500 cm3
        } else {
            rate = getShippingRate(ShippingRule.LARGE_PARCEL);
            shippingCalculator = new LargeParcelDecorator(volume, rate); // Large Parcel: Volume is greater than 2500 cm3
        }

        shippingCost = shippingCalculator.calculateCost();
       
        if (voucherCode != null && !voucherCode.isBlank()) {
            shippingCost = calculateShippingCostWithDiscount(shippingCost, voucherCode);
        }

        return new BigDecimal(shippingCost).setScale(2, RoundingMode.HALF_UP);
    }

    private double calculateShippingCostWithDiscount(double currentShippingCost, String voucherCode) {
        var discountPercentage = shippingDiscountService.getShippingDiscount(voucherCode);
        var discount = currentShippingCost * discountPercentage;
        var shippingCost = currentShippingCost - discount;
        return shippingCost;
    }

    private double getShippingRate(ShippingRule shippingRule) throws ResourceNotFoundException {
        var shippingRate = shippingRateRepository.findByRuleName(shippingRule)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping rate", "rule name", shippingRule.name()));
        return shippingRate.getRate();
    }

}
