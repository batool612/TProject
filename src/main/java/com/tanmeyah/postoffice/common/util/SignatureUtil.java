package com.tanmeyah.postoffice.common.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class SignatureUtil {

    private SignatureUtil() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    /**
     * Generates API signature:
     * RqUID + NID + SenderCode + SecurityKey
     * then SHA-256 + Base64
     */
    public static String generateSignature(String rqUID,
                                           String nid,
                                           String senderCode,
                                           String securityKey) {

        StringBuilder sb = new StringBuilder();

        // Append in strict order with trim
        if (rqUID != null) sb.append(rqUID.trim());
        if (nid != null) sb.append(nid.trim());
        if (senderCode != null) sb.append(senderCode.trim());
        if (securityKey != null) sb.append(securityKey.trim());

        String rawString = sb.toString();

        return sha256Base64(rawString);
    }

    /**
     * SHA-256 hashing + Base64 encoding
     */
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