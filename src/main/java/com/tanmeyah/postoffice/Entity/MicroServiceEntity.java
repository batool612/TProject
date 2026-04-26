package com.tanmeyah.postoffice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MICRO_SERVICES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MicroServiceEntity {

    @EmbeddedId
    private MicroServiceId id;

    @Column(name = "CUST_ID")
    private String custId;

    @Column(name = "STATUS")
    private Integer status;
}
