package com.tanmeyah.postoffice.DTO.Requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentNotificationRequestDTO {
    @NotBlank
    @JsonProperty("ReqUID")
    private String reqUID;

    @NotBlank
    @JsonProperty("SenderCode")
    private String senderCode;

    @NotBlank
    @JsonProperty("NID")
    private String nid;

    @NotBlank
    @JsonProperty("Amount")
    private String amount;

    @NotBlank
    @JsonProperty("TRXUID")
    private String trxUID;

    @NotBlank
    @JsonProperty("PostOfficeBranch")
    private String postOfficeBranch;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("PaymentDate")
    private String paymentDate;

    @NotBlank
    @JsonProperty("Signature")
    private String signature;
}