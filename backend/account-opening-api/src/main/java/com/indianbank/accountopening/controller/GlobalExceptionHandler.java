package com.indianbank.accountopening.controller;

import com.indianbank.accountopening.dto.ErrorDetail;
import com.indianbank.accountopening.dto.ErrorResponse;
import com.indianbank.accountopening.exception.ConflictException;
import com.indianbank.accountopening.exception.NotFoundException;
import com.indianbank.accountopening.exception.UnauthorizedException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
        .map(this::toErrorDetail)
      .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("VALIDATION_ERROR", "Request validation failed", correlation(request), details));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
    List<ErrorDetail> details = ex.getConstraintViolations().stream()
        .map(v -> new ErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
      .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("VALIDATION_ERROR", "Request validation failed", correlation(request), details));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponse("UNAUTHORIZED", ex.getMessage(), correlation(request), List.of()));
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("IDEMPOTENCY_CONFLICT", ex.getMessage(), correlation(request), List.of()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("NOT_FOUND", ex.getMessage(), correlation(request), List.of()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("BAD_REQUEST", ex.getMessage(), correlation(request), List.of()));
  }

  private ErrorDetail toErrorDetail(FieldError fieldError) {
    return new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
  }

  private String correlation(HttpServletRequest request) {
    String header = request.getHeader("X-Correlation-Id");
    return header == null ? "unknown" : header;
  }
}
