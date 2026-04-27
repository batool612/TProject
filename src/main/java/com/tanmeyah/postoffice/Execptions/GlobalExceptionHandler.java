package com.tanmeyah.postoffice.Execptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String CODE_VALIDATION = "VALIDATION_ERROR";
    public static final String CODE_JSON = "INVALID_JSON";
    public static final String CODE_BAD_REQUEST = "BAD_REQUEST";
    public static final String CODE_METHOD = "METHOD_NOT_ALLOWED";
    public static final String CODE_INTERNAL = "INTERNAL_ERROR";

    // ================= VALIDATION (BODY) =================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(
                        error.getField(),
                        error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                )
        );

        return build(
                HttpStatus.BAD_REQUEST,
                CODE_VALIDATION,
                "Request validation failed",
                fieldErrors
        );
    }




    // ================= VALIDATION (PARAMS) =================

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {

        Map<String, String> fieldErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        return build(
                HttpStatus.BAD_REQUEST,
                CODE_VALIDATION,
                "Constraint validation failed",
                fieldErrors
        );
    }

    // ================= CUSTOM BUSINESS VALIDATION =================

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomValidation(ValidationException ex) {

        return build(
                HttpStatus.BAD_REQUEST,
                CODE_VALIDATION,
                ex.getMessage() != null ? ex.getMessage() : "Validation failed",
                ex.getFieldErrors()
        );
    }

    // ================= INVALID JSON =================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {

        log.debug("Malformed JSON request: {}", ex.getMessage());

        return build(
                HttpStatus.BAD_REQUEST,
                CODE_JSON,
                "Request body is invalid or malformed JSON",
                null
        );
    }

    // ================= MISSING PARAM =================

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {

        Map<String, String> errors = Map.of(
                ex.getParameterName(), "Parameter is required"
        );

        return build(
                HttpStatus.BAD_REQUEST,
                CODE_VALIDATION,
                "Missing request parameter",
                errors
        );
    }

    // ================= TYPE MISMATCH =================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        Map<String, String> errors = Map.of(
                ex.getName(), "Invalid value type"
        );

        return build(
                HttpStatus.BAD_REQUEST,
                CODE_VALIDATION,
                "Parameter type mismatch",
                errors
        );
    }

    // ================= METHOD NOT ALLOWED =================

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {

        return build(
                HttpStatus.METHOD_NOT_ALLOWED,
                CODE_METHOD,
                "HTTP method not supported",
                null
        );
    }

    // ================= FALLBACK =================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnhandled(Exception ex) {

        log.error("Unhandled exception occurred", ex);

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CODE_INTERNAL,
                "Something went wrong. Please try again later.",
                null
        );
    }

    // ================= HELPER =================

private ResponseEntity<ApiErrorResponse> build(HttpStatus status,
                                               String code,
                                               String message,
                                               Map<String, String> fieldErrors) {

    ApiErrorResponse body = ApiErrorResponse.builder()
            .timestamp(Instant.now())
            .status(status.value()) // keep real status inside body
            .code(code)
            .message(message)
            .fieldErrors(fieldErrors)
            .build();

    //return ResponseEntity.status(status).body(body);
    // ALWAYS return 200
    return ResponseEntity.ok(body);
}
}