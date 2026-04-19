package com.tanmeyah.postoffice.DTO.Requests;

import com.tanmeyah.postoffice.Validation.ValidEgyptianNationalId;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InquiryRequestDTO {

    @NotBlank
    private String reqUID;

    @NotBlank
    @ValidEgyptianNationalId
    private String nid;

    @NotBlank
    private String senderCode;

    @NotBlank
    private String signature;
}