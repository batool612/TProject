package com.tanmeyah.postoffice.Service;

import com.tanmeyah.postoffice.DTO.Requests.InquiryRequestDTO;
import com.tanmeyah.postoffice.DTO.Reponses.InquiryResponseDTO;
import com.tanmeyah.postoffice.DTO.Projection.InquiryProjection;
import com.tanmeyah.postoffice.Repository.CashoutLoanDisburseRepository;
import com.tanmeyah.postoffice.Repository.DigitalPaymentsConfigurationRepository;
import com.tanmeyah.postoffice.Validation.EgyptianNationalIdValidator;
import com.tanmeyah.postoffice.common.enums.InquiryStatus;
import com.tanmeyah.postoffice.common.util.SignatureUtil;
import com.tanmeyah.postoffice.constants.DbConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AppServicelmp1 implements AppService {

    private final CashoutLoanDisburseRepository cashoutLoanDisburseRepository;
    private final DigitalPaymentsConfigurationRepository digitalPaymentsConfigurationRepository;
    private final EgyptianNationalIdValidator egyptianNationalIdValidator ;


    @Override
    public InquiryResponseDTO processInquiry(InquiryRequestDTO request) {
        if (request == null) {
            return buildResponse(null, null, null, null, InquiryStatus.AUTH_ERROR, null);
        }

        //to remove spaces and remove null
        String reqUID = trim(request.getReqUID());
        String nid = trim(request.getNid());
        String senderCode = trim(request.getSenderCode());
        String requestSignature = trim(request.getSignature());

        Map<String, String> configMap = digitalPaymentsConfigurationRepository
                .findByOperationTypeAndProviderCode(DbConstants.OPERATION_TYPE_CASHOUT, DbConstants.PROVIDER_POST_OFFICE)
                .stream()
                .collect(Collectors.toMap(
                        c -> normalizeKey(c.getConfigKey()),
                        c -> trim(c.getConfigValue()),
                        (a, b) -> a
                ));

        String configuredSenderCode = configMap.get("SENDER_CODE");
        String securityKey = configMap.get("SECUREACCTKEY");

        if (configuredSenderCode == null || securityKey == null) {
            return buildResponse(reqUID, nid, null, null, InquiryStatus.AUTH_ERROR, null);
        }


        if (!isValidNid(nid)) {
            return buildResponse(reqUID, nid, null, null, InquiryStatus.INVALID_NID_FORMAT, securityKey);
        }

        String expectedRequestSignature = SignatureUtil.generateInquiryRequestSignature(reqUID, nid, senderCode, securityKey);
        if (!expectedRequestSignature.equals(requestSignature)) {
            return buildResponse(reqUID, nid, null, null, InquiryStatus.AUTH_ERROR, securityKey);
        }


        Optional<InquiryProjection> inquiryResult = cashoutLoanDisburseRepository.findValidInquiry(
                nid,
                DbConstants.STATUS_ACTIVE,
                DbConstants.DISBURSE_TYPE_CASHOUT,
                DbConstants.PROVIDER_POST_OFFICE,
                DbConstants.FLAG_ACTIVE
        );


        if (inquiryResult.isEmpty()) {
            return buildResponse(reqUID, nid, null, null, InquiryStatus.NID_NOT_FOUND, securityKey);
        }
        InquiryProjection projection = inquiryResult.get();

        if (projection.getTotalAmt() == null || projection.getTotalAmt() <= 0) {
            return buildResponse(reqUID, nid, null, null, InquiryStatus.NOT_ALLOWED, securityKey);
        }

        return buildResponse(reqUID, nid, projection.getFullname(), projection.getTotalAmt(), InquiryStatus.SUCCESS, securityKey);
    }

    private InquiryResponseDTO buildResponse(String reqUID,
                                             String nid,
                                             String customerName,
                                             Double amount,
                                             InquiryStatus status,
                                             String securityKey) {
        return InquiryResponseDTO.builder()
                .reqUID(reqUID)
                .customerName(customerName)
                .nid(nid)
                .amount(amount)
                .statusCode(status.getCode())
                .statusDesc(status.getDescription())
                .signature(SignatureUtil.generateInquiryResponseSignature(reqUID, nid, status.getCode(), securityKey))
                .build();
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isValidNid(String nid) {
        return egyptianNationalIdValidator.isValid(nid, null);
    }

    private boolean isValidRequestSignature(String reqUID,
                                            String nid,
                                            String senderCode,
                                            String requestSignature,
                                            String securityKey) {
        if (requestSignature == null || requestSignature.isBlank()) {
            return false;
        }
        String expectedRequestSignature = SignatureUtil.generateInquiryRequestSignature(reqUID, nid, senderCode, securityKey);
        return expectedRequestSignature.equals(requestSignature.trim());
    }

    private String normalizeKey(String key) {
        return trim(key).toUpperCase();
    }
}