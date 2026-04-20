package com.tanmeyah.postoffice.DTO.Reponses;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class InquiryResponseDTO {
    @JsonProperty("ReqUID")
    private String reqUID;
    @JsonProperty("CustomerName")
    private String customerName;  //only if success
    @JsonProperty("NID")
    private String nid;
    @JsonProperty("Amount")
    private Double amount;  //only if success
    @JsonProperty("StatusCode")
    private Integer statusCode;
    @JsonProperty("StatusDesc")
    private String statusDesc;
    @JsonProperty("Signature")
    private String signature;
}