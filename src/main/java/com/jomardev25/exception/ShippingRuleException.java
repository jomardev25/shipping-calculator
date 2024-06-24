package com.jomardev25.exception;

public class ShippingRuleException extends RuntimeException {

    public ShippingRuleException() {
        super("Invalid weight. Weight exceeds 50kg.");
    }

}
