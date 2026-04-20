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
@Table(name = "APIS_DIGITAL_PAYMENTS_CONFIGURATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DigitalPaymentsConfigurationEntity {

    @Id
    @Column(name = "PK_SEQ")
    private Integer pk;

    @Column(name = "\"KEY\"")
    private String configKey;

    @Column(name = "\"VALUE\"")
    private String configValue;
//
//    @Column(name = "KEY")
//    private String configKey;
//
//    @Column(name = "VALUE")
//    private String configValue;

    @Column(name = "OPERATION_TYPE")
    private Integer operationType;

    @Column(name = "PROVIDER_CODE")
    private Integer providerCode;
}