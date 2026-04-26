package com.tanmeyah.postoffice.Execptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    Instant timestamp;
    int status;
    String code;
    String message;
    /** Present for validation failures: field or property path → message */
    Map<String, String> fieldErrors;
}