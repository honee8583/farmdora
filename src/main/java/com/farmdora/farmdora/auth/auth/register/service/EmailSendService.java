package com.farmdora.farmdora.auth.auth.register.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {

    private final JavaMailSender mailSender;

    public EmailSendService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String verificationCode,String title, String content) {
        try {
            // MimeMessage 객체 생성
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // HTML 내용 작성
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f3f4f6;\">"
                    + "    <div style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; "
                    + "                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); text-align: center; padding: 20px;\">"
                    + "        <div style=\"background-color: #1CA673; color: white; padding: 20px; font-size: 24px; font-weight: bold;\">"
                    +             title
                    + "        </div>"
                    + "        <div style=\"margin: 20px 0; color: #333; font-size: 16px;\">"
                    + "            <p>"+content+"</p>"
                    + "            <p style=\"font-size: 2em; color: #4CAF50; font-weight: bold; margin: 20px 0;\">" + verificationCode + "</p>"
                    + "        </div>"
                    + "        <div style=\"font-size: 12px; color: #888; margin-top: 20px; padding-top: 10px; border-top: 1px solid #ddd;\">"
                    + "            <p>본 메일은 자동으로 발송되었습니다. 문의 사항이 있으시면 고객 지원팀에 연락하세요.</p>"
                    + "        </div>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";

            // 이메일 설정
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML 내용으로 설정

            // 이메일 발송
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패", e);
        }
    }

}
