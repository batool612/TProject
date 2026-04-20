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
@Table(name = "micro_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MicroServiceEntity {

    @Id
    @Column(name = "SERVICE_ID")
    private String serviceId;
    @Column(name = "CUST_ID")
    private String custId;
    @Column(name = "STATUS")
    private Integer status;
}
