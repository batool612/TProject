package com.tanmeyah.postoffice.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "customers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    @Column(name = "CUSTNO")
    private Integer cusNumber;
    @Column(name = "ID_NUMBER")
    private String idNumber;
    @Column(name = "FULLNAME")
    private String fullName;
}