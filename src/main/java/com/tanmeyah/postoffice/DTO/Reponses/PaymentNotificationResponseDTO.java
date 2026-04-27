package com.tanmeyah.postoffice.DTO.Reponses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
@JsonPropertyOrder({
        "ReqUID",
        "TRXUID",
        "StatusCode",
        "StatusDesc",
        "Signature"
})
public class PaymentNotificationResponseDTO {
    @JsonProperty("ReqUID")
    private String reqUID;

    @JsonProperty("TRXUID")
    private String trxUID;

    @JsonProperty("StatusCode")
    private Integer statusCode;

    @JsonProperty("StatusDesc")
    private String statusDesc;

    @JsonProperty("Signature")
    private String signature;
}