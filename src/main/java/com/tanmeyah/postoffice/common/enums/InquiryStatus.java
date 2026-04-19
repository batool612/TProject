package com.tanmeyah.postoffice.common.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryStatus {

    SUCCESS(200, "Success"),
    INVALID_SENDER(46, "Sender is not authorized or is inactive"),
    INVALID_NID_FORMAT(13011, "Invalid NID (Length or Formatting)"),
    NID_NOT_FOUND(13006, "Customer NID is not existed"),
    NOT_ALLOWED(13017, "Customer is not allowed to proceed with the disbursement"),
    AUTH_ERROR(37, "Message Authentication Error");

    private final int code;
    private final String description;
}