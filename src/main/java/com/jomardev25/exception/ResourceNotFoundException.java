package com.jomardev25.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String resource;
	private String field;
	private Object value;

	public ResourceNotFoundException(String resource, String field, Object value) {
		super(String.format("%s was not found with %s: %s", resource, field, value));
		this.resource = resource;
		this.field = field;
		this.value = value;
	}

}