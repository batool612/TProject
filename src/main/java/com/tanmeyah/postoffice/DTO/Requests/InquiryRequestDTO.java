package com.tanmeyah.postoffice.DTO.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanmeyah.postoffice.Validation.ValidEgyptianNationalId;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InquiryRequestDTO {
    @NotBlank
    @JsonProperty("ReqUID")
    private String reqUID;

    @NotBlank
    @ValidEgyptianNationalId
    @JsonProperty("NID")
    private String nid;

    @NotBlank
    @JsonProperty("SenderCode")
    private String senderCode;

    @NotBlank
    @JsonProperty("Signature")
    private String signature;
}