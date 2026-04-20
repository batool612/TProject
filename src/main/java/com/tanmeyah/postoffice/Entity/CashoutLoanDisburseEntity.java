package com.tanmeyah.postoffice.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "APIS_CASHOUT_LOAN_DISBURSE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashoutLoanDisburseEntity {

    @Id
    @Column(name = "DRC")
    private String drc;

    @Column(name = "IDNO")
    private String idno;

    @Column(name = "MOBILEPHONE")
    private String mobilephone;

    @Column(name = "TOTAL_AMT")
    private Double totalAmt;

    @Column(name = "DISBURSE_TYPE_CODE")
    private Integer disburseTypeCode;

    @Column(name = "PROVIDER_CODE")
    private Integer providerCode;

    @Column(name = "FLAG")
    private Integer flag;
}