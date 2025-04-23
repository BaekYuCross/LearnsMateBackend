package intbyte4.learnsmate.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class RedisKeyHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String buildPaymentCacheKey(PaymentFilterRequestVO request, int page, int size, String sortField, String sortDirection) {
        try {
            // 1. 필터 + 페이지 정보 조합한 객체 → JSON 문자열로 직렬화
            ObjectMapper objectMapper = new ObjectMapper();

            // 필터 객체가 null이면 빈 객체 처리 (or "{}" 처리)
            String filterJson = (request != null) ? objectMapper.writeValueAsString(request) : "{}";

            String rawKey = objectMapper.writeValueAsString(request) +
                    String.format(":page=%d:size=%d:sort=%s_%s", page, size, sortField, sortDirection);

            // 2. SHA-256 해시 생성
            return "payments:" + sha256(rawKey);

        } catch (Exception e) {
            throw new RuntimeException("캐시 키 생성 중 오류 발생", e);
        }
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}