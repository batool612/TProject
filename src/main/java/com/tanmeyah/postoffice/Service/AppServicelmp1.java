package com.tanmeyah.postoffice.Service;

import com.tanmeyah.postoffice.DTO.Reponses.PaymentNotificationResponseDTO;
import com.tanmeyah.postoffice.DTO.Requests.InquiryRequestDTO;
import com.tanmeyah.postoffice.DTO.Reponses.InquiryResponseDTO;
import com.tanmeyah.postoffice.DTO.Projection.InquiryProjection;
import com.tanmeyah.postoffice.DTO.Requests.PaymentNotificationRequestDTO;
import com.tanmeyah.postoffice.Entity.CashoutLoanDisburseEntity;
import com.tanmeyah.postoffice.Repository.CashoutLoanDisburseRepository;
import com.tanmeyah.postoffice.Repository.DigitalPaymentsConfigurationRepository;
import com.tanmeyah.postoffice.Validation.EgyptianNationalIdValidator;
import com.tanmeyah.postoffice.common.enums.Status;
import com.tanmeyah.postoffice.common.util.InquirySignatureUtil;
import com.tanmeyah.postoffice.common.util.PaymentNotificationSignatureUtil;
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
            return buildResponse(null, null, null, null, Status.AUTH_ERROR, null);
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
            return buildResponse(reqUID, nid, null, null, Status.AUTH_ERROR, null);
        }

        if (!isValidNid(nid)) {
            return buildResponse(reqUID, nid, null, null, Status.INVALID_NID_FORMAT, securityKey);
        }

        String expectedRequestSignature = InquirySignatureUtil.generateInquiryRequestSignature(reqUID, nid, senderCode, securityKey);
        if (!expectedRequestSignature.equals(requestSignature)) {
            return buildResponse(reqUID, nid, null, null, Status.AUTH_ERROR, securityKey);
        }

        Optional<InquiryProjection> inquiryResult = cashoutLoanDisburseRepository.findValidInquiry(
                nid,
                DbConstants.STATUS_ACTIVE,
                DbConstants.DISBURSE_TYPE_CASHOUT,
                DbConstants.PROVIDER_POST_OFFICE,
                DbConstants.FLAG_ACTIVE
        );

        if (inquiryResult.isEmpty()) {
            return buildResponse(reqUID, nid, null, null, Status.NID_NOT_FOUND, securityKey);
        }
        InquiryProjection projection = inquiryResult.get();

        if (projection.getTotalAmt() == null || projection.getTotalAmt() <= 0) {
            return buildResponse(reqUID, nid, null, null, Status.NOT_ALLOWED, securityKey);
        }

        return buildResponse(reqUID, nid, projection.getFullName(), projection.getTotalAmt(), Status.SUCCESS, securityKey);
    }

    private InquiryResponseDTO buildResponse(String reqUID,
                                             String nid,
                                             String customerName,
                                             Double amount,
                                             Status status,
                                             String securityKey) {
        return InquiryResponseDTO.builder()
                .reqUID(reqUID)
                .customerName(customerName)
                .nid(nid)
                .amount(amount)
                .statusCode(status.getCode())
                .statusDesc(status.getDescription())
                .signature(InquirySignatureUtil.generateInquiryResponseSignature(reqUID, nid, status.getCode(), securityKey))
                .build();
    }



    public PaymentNotificationResponseDTO processPaymentNotification(PaymentNotificationRequestDTO request) {
        if (request == null) {
            return buildPaymentNotificationResponse(null, null, Status.AUTH_ERROR, null);
        }

        String reqUID = trim(request.getReqUID());
        String senderCode = trim(request.getSenderCode());
        String nid = trim(request.getNid());
        String amount = trim(request.getAmount());
        String trxUID = trim(request.getTrxUID());
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
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.AUTH_ERROR, null);
        }

        if (!isValidNid(nid)) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.INVALID_NID_FORMAT, securityKey);
        }
        String expectedRequestSignature = PaymentNotificationSignatureUtil.generatePaymentNotificationRequestSignature(
                reqUID,
                nid,
                senderCode,
                amount,
                trxUID,
                securityKey
        );
        if (!expectedRequestSignature.equals(requestSignature)) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.AUTH_ERROR, securityKey);
        }

        if (!configuredSenderCode.equals(senderCode)) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.INVALID_SENDER, securityKey);
        }


        Optional<InquiryProjection> readyForDisbursement =
                cashoutLoanDisburseRepository.findValidInquiry(
                        nid,
                        DbConstants.STATUS_ACTIVE,
                        DbConstants.DISBURSE_TYPE_CASHOUT,
                        DbConstants.PROVIDER_POST_OFFICE,
                        DbConstants.FLAG_ACTIVE
                );

        if (readyForDisbursement.isEmpty()) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.NOT_ALLOWED, securityKey);
        }

        Double requestAmount;
        try {
            requestAmount = Double.valueOf(amount);
        } catch (NumberFormatException ex) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.INVALID_AMOUNT, securityKey);
        }

        InquiryProjection projection = readyForDisbursement.get();
        if (projection.getTotalAmt() == null || Double.compare(projection.getTotalAmt(), requestAmount) != 0) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.INVALID_AMOUNT, securityKey);
        }


        boolean duplicateTransaction = cashoutLoanDisburseRepository.existsByIdnoAndDisburseTypeCodeAndProviderCodeAndTotalAmtAndFlag(
                nid,
                DbConstants.DISBURSE_TYPE_CASHOUT,
                DbConstants.PROVIDER_POST_OFFICE,
                requestAmount,
                DbConstants.FLAG_PAYMENT_NOTIFIED
        );
        if (duplicateTransaction) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.NOT_ALLOWED, securityKey);
        }

        Optional<CashoutLoanDisburseEntity> entityOptional =
                cashoutLoanDisburseRepository.findByIdnoAndDisburseTypeCodeAndProviderCodeAndFlag(
                        nid,
                        DbConstants.DISBURSE_TYPE_CASHOUT,
                        DbConstants.PROVIDER_POST_OFFICE,
                        DbConstants.FLAG_ACTIVE
                );
        if (entityOptional.isEmpty()) {
            return buildPaymentNotificationResponse(reqUID, trxUID, Status.NOT_ALLOWED, securityKey);
        }

        CashoutLoanDisburseEntity entity = entityOptional.get();
        entity.setFlag(DbConstants.FLAG_PAYMENT_NOTIFIED);
        cashoutLoanDisburseRepository.save(entity);

        return buildPaymentNotificationResponse(reqUID, trxUID, Status.SUCCESS, securityKey);

    }
    private PaymentNotificationResponseDTO buildPaymentNotificationResponse(String reqUID,
                                                                            String trxUID,
                                                                            Status status,
                                                                            String securityKey) {
        return PaymentNotificationResponseDTO.builder()
                .reqUID(reqUID)
                .trxUID(trxUID)
                .statusCode(status.getCode())
                .statusDesc(status.getDescription())
                .signature(PaymentNotificationSignatureUtil.generatePaymentNotificationResponseSignature(
                        reqUID,
                        trxUID,
                        status.getCode(),
                        securityKey
                ))
                .build();
    }



    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isValidNid(String nid) {
        return egyptianNationalIdValidator.isValid(nid, null);
    }
    private String normalizeKey(String key) {
        return trim(key).toUpperCase();
    }
}