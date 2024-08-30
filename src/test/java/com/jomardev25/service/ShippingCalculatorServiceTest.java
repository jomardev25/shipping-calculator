package com.jomardev25.service;

import com.jomardev25.entity.ShippingRate;
import com.jomardev25.entity.ShippingRule;
import com.jomardev25.exception.ResourceNotFoundException;
import com.jomardev25.exception.ShippingRuleException;
import com.jomardev25.repository.ShippingRateRepository;
import com.jomardev25.service.impl.ShippingCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class ShippingCalculatorServiceTest {

    @InjectMocks
    private ShippingCalculatorServiceImpl shippingCalculatorService;

    @Mock
    private ShippingRateRepository shippingRateRepository;

    @Mock
    private ShippingDiscountService shippingDiscountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateShippingCost_HeavyParcel() {
        var weight = 50.0;
        var height = 50.0;
        var width = 5.0;
        var length = 4.0;
        var expectedCost = new BigDecimal("1000.00");
        var shippingRate = ShippingRate.builder()
                .id(1)
                .ruleName(ShippingRule.HEAVY_PARCEL)
                .rate(20.0)
                .condition("Weight exceeds 10kg")
                .build();


        when(shippingRateRepository.findByRuleName(ShippingRule.HEAVY_PARCEL)).thenReturn(Optional.of(shippingRate));

        var cost = shippingCalculatorService.calculateShippingCost(weight, height, width, length, null);

        assertThat(cost).isEqualByComparingTo(expectedCost);
    }

    @Test
    public void testCalculateShippingCost_WithDiscount() {
        var weight = 5.0;
        var height = 10.0;
        var width = 10.0;
        var length = 10.0;
        var voucherCode = "DISCOUNT10";
        var discountPercentage = 0.1;
        var expectedCost = new BigDecimal("27.00");

        var shippingRate = ShippingRate.builder()
                .id(3)
                .ruleName(ShippingRule.SMALL_PARCEL)
                .rate(0.03)
                .condition("Volume is less than 1500 cm3")
                .build();

        when(shippingRateRepository.findByRuleName(ShippingRule.SMALL_PARCEL)).thenReturn(Optional.of(shippingRate));
        when(shippingDiscountService.getShippingDiscount(voucherCode)).thenReturn(discountPercentage);

        var cost = shippingCalculatorService.calculateShippingCost(weight, height, width, length, voucherCode);

        assertThat(cost).isEqualByComparingTo(expectedCost);
    }

    @Test
    public void testCalculateShippingCost_WeightExceedsLimit() {
        double weight = 55.0;
        double height = 10.0;
        double width = 10.0;
        double length = 10.0;

        assertThatThrownBy(() -> shippingCalculatorService.calculateShippingCost(weight, height, width, length, null))
                .isInstanceOf(ShippingRuleException.class);
    }

    @Test
    public void testGetShippingRate_ResourceNotFound() {
        double weight = 5.0;
        double height = 10.0;
        double width = 10.0;
        double length = 10.0;

        when(shippingRateRepository.findByRuleName(ShippingRule.SMALL_PARCEL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shippingCalculatorService.calculateShippingCost(weight, height, width, length, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"param1", "param2", "param3"})
    public void testParameterized(String value) {
        System.out.println(value);
    }
}
