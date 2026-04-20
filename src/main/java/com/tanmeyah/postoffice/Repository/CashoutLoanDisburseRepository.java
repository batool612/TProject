package com.tanmeyah.postoffice.Repository;

import com.tanmeyah.postoffice.Entity.CashoutLoanDisburseEntity;
import com.tanmeyah.postoffice.Projection.InquiryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashoutLoanDisburseRepository extends JpaRepository<CashoutLoanDisburseEntity, Long> {

    @Query(value = """
    select
        ac.IDNO as idno,
        ac.MOBILEPHONE as mobilephone,
        c.FullNAME as fullname,
        ac.DRC as drc,
        ac.TOTAL_AMT as totalAmt
    from APIS_CASHOUT_LOAN_DISBURSE ac
    join micro_services ms on ac.drc = ms.SERVICE_ID
    join customers c on ms.cust_id = c.id_number
    where ms.STATUS = :status
      and ac.DISBURSE_TYPE_CODE = :disburseType
      and ac.PROVIDER_CODE = :providerCode
      and ac.idno = :nid
      and ac.flag = :flag
""", nativeQuery = true)
    Optional<InquiryProjection> findValidInquiry(
            @Param("nid") String nid,
            @Param("status") Integer status,
            @Param("disburseType") Integer disburseType,
            @Param("providerCode") Integer providerCode,
            @Param("flag") Integer flag
    );
}

