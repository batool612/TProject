package com.tanmeyah.postoffice.DTO.Projection;

import java.math.BigDecimal;

public interface InquiryProjection {
    String getIdno();
    String getMobilephone();
    String getFullName();
    Integer getDrc();
    BigDecimal getTotalAmt();
}
