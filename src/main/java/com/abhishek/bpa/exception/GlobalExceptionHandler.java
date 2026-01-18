package com.abhishek.bpa.exception;

import com.abhishek.bpa.dto.common.ApiResponse;
import com.abhishek.bpa.dto.common.ResponseStatus;
import com.abhishek.bpa.dto.common.ValidationError;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Hidden
@RestControllerAdvice(basePackages = "com.abhishek.bpa")
@Slf4j
public class GlobalExceptionHandler {

    /** Business exception can throw only one error **/
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(
            BusinessException e, HttpServletRequest request
    ){

        log.warn("Business exception at [{}] : {}", request.getRequestURI(), e.getMessage());

        ResponseStatus status = ResponseStatus.builder()
                .success(false)
                .httpStatus(e.getHttpStatus())
                .message(e.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.builder()
                        .responseStatus(status)
                        .data(null)
                        .build());
    }

    /** Validation exception can throw multiple errors */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ){

        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        log.warn("Validation failed at [{}] with {} error(s)", request.getRequestURI(), e.getBindingResult().getErrorCount());
        log.debug("Validation details: {}", e.getBindingResult().getFieldErrors());

        ResponseStatus status = ResponseStatus.builder()
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .responseStatus(status)
                        .data(errors)
                        .build());
    }

    /** DB Constraint violations */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(
            DataIntegrityViolationException e,
            HttpServletRequest request
    ){

        log.warn("DB constraint violation at [{}] : {}", request.getRequestURI(), e.getMostSpecificCause().getMessage());

        ResponseStatus status = ResponseStatus.builder()
                .success(false)
                .httpStatus(HttpStatus.CONFLICT.value())
                .message("Duplicate or invalid data")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.builder()
                        .responseStatus(status)
                        .data(null)
                        .build());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse> handleAuthorizationDenied(
            org.springframework.security.authorization.AuthorizationDeniedException e,
            HttpServletRequest request
    ) {

        log.warn("Access denied at [{}]", request.getRequestURI());

        ResponseStatus status = ResponseStatus.builder()
                .success(false)
                .httpStatus(HttpStatus.FORBIDDEN.value())
                .message("Access Denied")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.builder()
                        .responseStatus(status)
                        .data(null)
                        .build());
    }

    /** System errors */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneric(
            Exception e,
            HttpServletRequest request
    ) {

        String path = request.getRequestURI();

        // Let spring doc handle its own errors
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            throw new RuntimeException(e);
        }

        log.error("Unhandled system errors at [{}]", path, e);

        ResponseStatus status = ResponseStatus.builder()
                .success(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error. Please try again later.")
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .responseStatus(status)
                        .data(null)
                        .build());
    }

}
