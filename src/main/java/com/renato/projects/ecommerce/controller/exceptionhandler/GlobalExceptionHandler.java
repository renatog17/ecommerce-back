package com.renato.projects.ecommerce.controller.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
