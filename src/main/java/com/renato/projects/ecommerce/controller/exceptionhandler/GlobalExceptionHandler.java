package com.renato.projects.ecommerce.controller.exceptionhandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiError>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        err.getDefaultMessage(),
                        err.getField(),
                        request.getRequestURI()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors);
    }
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, Object>> handleResponseStatusException(
	        ResponseStatusException ex,
	        HttpServletRequest request) {

	    Map<String, Object> body = new HashMap<>();
	    body.put("timestamp", LocalDateTime.now());
	    body.put("status", ex.getStatusCode().value());
	    body.put("error", ex.getStatusCode().toString());
	    body.put("message", ex.getReason());
	    body.put("path", request.getRequestURI());

	    return ResponseEntity
	            .status(ex.getStatusCode())
	            .body(body);
	}
}
