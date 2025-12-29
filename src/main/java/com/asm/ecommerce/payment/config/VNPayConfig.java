package com.asm.ecommerce.payment.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class VNPayConfig {
    
    @Value("${vnpay.tmn-code}")
    private String tmnCode;
    
    @Value("${vnpay.hash-secret}") 
    private String hashSecret;
    
    @Value("${vnpay.pay-url}")
    private String payUrl;
    
    @Value("${vnpay.return-url}")
    private String returnUrl;

    @Value("${vnpay.ipn-url}")
    private String ipnUrl;

    @Value("${vnpay.api-url}")
    private String apiUrl;

    public void printConfig() {
        System.out.println("=== VNPay Config ===");
        System.out.println("TmnCode: " + tmnCode);
        System.out.println("HashSecret: " + (hashSecret != null ? "***" + hashSecret.substring(Math.max(0, hashSecret.length() - 4)) : "NULL"));
        System.out.println("ReturnURL: " + returnUrl);
        System.out.println("===================");
    }
}