package com.tanmeyah.postoffice.DTO.Reponses;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@JsonPropertyOrder({
        "ReqUID",
        "CustomerName",
        "NID",
        "Amount",
        "StatusCode",
        "StatusDesc",
        "Signature"
})
public class InquiryResponseDTO {
    @JsonProperty("ReqUID")
    private String reqUID;
    @JsonProperty("CustomerName")
    private String customerName;  //only if success
    @JsonProperty("NID")
    private String nid;
    @JsonProperty("Amount")
    private BigDecimal amount;  //only if success
    @JsonProperty("StatusCode")
    private Integer statusCode;
    @JsonProperty("StatusDesc")
    private String statusDesc;
    @JsonProperty("Signature")
    private String signature;
}