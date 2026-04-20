package com.tanmeyah.postoffice.Repository;

import com.tanmeyah.postoffice.Entity.CashoutLoanDisburseEntity;
import com.tanmeyah.postoffice.Projection.InquiryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashoutLoanDisburseRepository extends JpaRepository<CashoutLoanDisburseEntity, String> {
    @Query("""
    select 
        ac.idno as idno,
        ac.mobilephone as mobilephone,
        c.fullName as fullname,
        ac.drc as drc,
        ac.totalAmt as totalAmt
    from CashoutLoanDisburseEntity ac
    join MicroServiceEntity ms on ac.drc = ms.serviceId
    join CustomerEntity c on ms.custId = c.idNumber
    where ms.status = :status
      and ac.disburseTypeCode = :disburseType
      and ac.providerCode = :providerCode
      and ac.idno = :nid
      and ac.flag = :flag
""")
    Optional<InquiryProjection> findValidInquiry(
            String nid,
            Integer status,
            Integer disburseType,
            Integer providerCode,
            Integer flag
    );
}
