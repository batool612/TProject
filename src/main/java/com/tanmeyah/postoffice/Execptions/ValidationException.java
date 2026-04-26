package com.tanmeyah.postoffice.Execptions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = null;
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors == null
                ? null
                : Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    public static ValidationException forField(String field, String message) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put(field, message);
        return new ValidationException(message, m);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors == null ? Collections.emptyMap() : fieldErrors;
    }
}