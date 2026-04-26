package com.tanmeyah.postoffice.Repository;
import com.tanmeyah.postoffice.Entity.DigitalPaymentsConfigurationEntity;
import com.tanmeyah.postoffice.DTO.Projection.DigitalPaymentConfigurationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DigitalPaymentsConfigurationRepository
        extends JpaRepository<DigitalPaymentsConfigurationEntity, String> {

    @Query("""
        select 
            c.configKey as configKey,
            c.configValue as configValue
        from DigitalPaymentsConfigurationEntity c 
        where c.operationType = :operationType
          and c.providerCode = :providerCode
    """)
    List<DigitalPaymentConfigurationProjection> findByOperationTypeAndProviderCode(
            Integer operationType,
            Integer providerCode
    );
}
