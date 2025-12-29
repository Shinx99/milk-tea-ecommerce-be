package com.asm.ecommerce.payment.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class VNPayUtil {

    private final VNPayConfig vnPayConfig;

    /**
     * Tạo URL thanh toán VNPay
     */
    public String createPayment(String orderCode,
                                long amount,              // tổng tiền đơn hàng
                                String orderInfo,
                                String orderType,
                                String ipAddress) throws Exception {

        //Map<String, String> vnpParams = new HashMap<>();
        Map<String, String> vnpParams = new TreeMap<>(); // Dùng TreeMap để tự động sort

        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100)); // VNPay yêu cầu *100
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderCode);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", ipAddress);
        vnpParams.put("vnp_CreateDate", getCurrentDate());

        // Nếu muốn set thời gian hết hạn (ví dụ +15 phút)
        Calendar expire = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        expire.add(Calendar.MINUTE, 15);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String expireDate = df.format(expire.getTime());
        vnpParams.put("vnp_ExpireDate", expireDate);

        // Nếu dùng IPN URL từ code (tuỳ có config trên portal hay không)
//        if (vnPayConfig.getIpnUrl() != null && !vnPayConfig.getIpnUrl().isEmpty()) {
//            vnpParams.put("vnp_IpnUrl", vnPayConfig.getIpnUrl());
//        }
        //vnpParams.put("vnp_IpnUrl", vnPayConfig.getIpnUrl());

        // Log thông tin để debug
        log.debug("[VNPay] Creating payment with params: {}", vnpParams);
        log.info("[VNPay] tmnCode={}, hashSecret={}...",
                vnPayConfig.getTmnCode(),
                vnPayConfig.getHashSecret().substring(0, Math.min(10, vnPayConfig.getHashSecret().length())));

        String query = buildSignedQuery(vnpParams, vnPayConfig.getHashSecret());

        String paymentUrl = vnPayConfig.getPayUrl() + "?" + query;
        log.info("Created VNPay URL for order: {}", orderCode);



        return paymentUrl;
    }

    /**
     * Validate chữ ký từ VNPay (dùng cho callback & IPN)
     */
    public boolean validateSignature(Map<String, String> vnpParams) {
        try {
            String receivedHash = vnpParams.get("vnp_SecureHash");
            if (receivedHash == null || receivedHash.isEmpty()) {
                return false;
            }

            // Tạo map mới, bỏ vnp_SecureHash và vnp_SecureHashType
            Map<String, String> fields = new HashMap<>();
            for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
                String key = entry.getKey();
                if ("vnp_SecureHash".equals(key) || "vnp_SecureHashType".equals(key)) {
                    continue;
                }
                fields.put(key, entry.getValue());
            }

            String unsignedQuery = buildHashData(fields);
            String calculatedHash = hmacSHA512(vnPayConfig.getHashSecret(), unsignedQuery);

            return calculatedHash.equalsIgnoreCase(receivedHash);

        } catch (Exception e) {
            log.error("Error validating VNPay signature", e);
            return false;
        }
    }

    /**
     * Build query có ký vnp_SecureHash (dùng khi tạo payment)
     */
    private String buildSignedQuery(Map<String, String> vnpParams, String secret) throws Exception {
        String hashData = buildHashData(vnpParams);
        String vnpSecureHash = hmacSHA512(secret, hashData);

        StringBuilder query = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnpParams.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                     .append("=")
                     .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (i != fieldNames.size() - 1) {
                    query.append("&");
                }
            }
        }

        query.append("&vnp_SecureHash=").append(vnpSecureHash);
        return query.toString();
    }

    /**
     * Build chuỗi hashData (sort key + nối key=value&...)
     */
    private String buildHashData(Map<String, String> vnpParams) throws Exception {
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnpParams.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName)
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (i != fieldNames.size() - 1) {
                    hashData.append("&");
                }
            }
        }
        return hashData.toString();
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(keySpec);
        byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return formatter.format(new Date());
    }
}
