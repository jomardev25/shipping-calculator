package com.jomardev25.exception;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
@Getter
public class ShippingDiscountException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
}
