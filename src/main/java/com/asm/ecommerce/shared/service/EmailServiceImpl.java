package com.asm.ecommerce.shared.service;

import com.asm.ecommerce.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText("Xin chào,\n\n" +
                    "Vui lòng nhấp vào liên kết sau để đặt lại mật khẩu của bạn:\n" +
                    resetLink + "\n\n" +
                    "Nếu không yêu cầu này, bạn có thể bỏ qua email này.\n\n" +
                    "Trân trọng,\nĐội ngũ phát triển.");
            mailSender.send(message);
        }catch (Exception e){
            log.error("Error sending email: {}",e.getMessage(), e);
        }
    }
}
