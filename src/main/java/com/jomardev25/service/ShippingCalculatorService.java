package com.jomardev25.service;

import java.math.BigDecimal;

public interface ShippingCalculatorService {
    BigDecimal calculateShippingCost(double weight, double height, double width, double length, String voucherCode);
}
