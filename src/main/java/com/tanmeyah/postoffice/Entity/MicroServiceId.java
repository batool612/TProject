package com.tanmeyah.postoffice.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MicroServiceId implements Serializable {

    @Column(name = "SERVICE_ID")
    private Integer serviceId;

    @Column(name = "LANG_CODE")
    private String langCode;

}
