package com.tanmeyah.postoffice.DTO.Reponses;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class InquiryResponseDTO {

    private String reqUID;
    private String customerName;  //only if success
    private String nid;
    private Double amount;  //only if success
    private Integer statusCode;
    private String statusDesc;
    private String signature;
}