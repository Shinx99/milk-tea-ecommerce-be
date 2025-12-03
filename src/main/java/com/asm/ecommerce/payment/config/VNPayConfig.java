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
}