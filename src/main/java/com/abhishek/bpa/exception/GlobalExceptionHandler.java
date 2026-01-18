package com.abhishek.bpa.exception;

import com.abhishek.bpa.dto.common.ApiResponse;
import com.abhishek.bpa.dto.common.ResponseStatus;
import com.abhishek.bpa.dto.common.ValidationError;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Hidden
@RestControllerAdvice(basePackages = "com.abhishek.bpa")
public class GlobalExceptionHandler {

    /** Business exception can throw only one error **/
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException e){

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
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e){

        List<ValidationError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

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
    public ResponseEntity<ApiResponse> handleConstraintViolation(){

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

        ResponseStatus status = ResponseStatus.builder()
                .success(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
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
