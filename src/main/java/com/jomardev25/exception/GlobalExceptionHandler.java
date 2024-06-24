package com.jomardev25.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jomardev25.dto.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest webRequest) {
				var errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ShippingRuleException.class)
	public ResponseEntity<ErrorResponseDTO> handleShippingRuleException(ShippingRuleException ex,
			WebRequest webRequest) {
				var errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ShippingDiscountException.class)
	public ResponseEntity<ErrorResponseDTO> handleShippingDiscountException(ShippingDiscountException ex, WebRequest webRequest) {
		var errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest webRequest) {
		var errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, String> errors = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			var field = ((FieldError) error).getField();
			var message = error.getDefaultMessage();
			errors.put(field, message);
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
