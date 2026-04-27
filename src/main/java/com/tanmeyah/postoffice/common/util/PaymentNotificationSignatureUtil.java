package com.tanmeyah.postoffice.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PaymentNotificationSignatureUtil {

    private PaymentNotificationSignatureUtil() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    public static String generatePaymentNotificationRequestSignature(String rqUID,
                                                                     String nid,
                                                                     String senderCode,
                                                                     String amount,
                                                                     String trxUID,
                                                                     String securityKey) {
        return generateFromParts(securityKey, rqUID, nid, senderCode, amount, trxUID);
    }

    public static String generatePaymentNotificationResponseSignature(String rqUID,
                                                                      String trxUID,
                                                                      Integer statusCode,
                                                                      String securityKey) {
        return generateFromParts(
                securityKey,
                rqUID,
                trxUID,
                statusCode == null ? null : String.valueOf(statusCode)
        );
    }

    private static String generateFromParts(String securityKey, String... parts) {
        StringBuilder sb = new StringBuilder();
        if (parts != null) {
            for (String part : parts) {
                if (part != null) {
                    sb.append(part.trim());
                }
            }
        }
        if (securityKey != null) {
            sb.append(securityKey.trim());
        }
        return sha256Base64(sb.toString());
    }

    private static String sha256Base64(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}