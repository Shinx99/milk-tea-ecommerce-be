package com.asm.ecommerce.shared.service;

public interface EmailService {

    /**
     * Gửi email reset mật khẩu
     * @param toEmail địa chỉ email người nhận
     * @param resetLink link đặt lại mật khẩu
     */
    void sendPasswordResetEmail(String toEmail, String resetLink);

}
