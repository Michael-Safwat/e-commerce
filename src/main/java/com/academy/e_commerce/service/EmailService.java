package com.academy.e_commerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    public void sendVerificationEmail(String toEmail, String name, String token) {
        String subject = "Verify Your Email";
        String verifyLink ="http://localhost:8080"+ baseUrl + "/users/verify?token=" + token;

        String html = """
            <p>Dear %s,</p>
            <p>Please click the button below to verify your account:</p>
            <p style="text-align: center;">
              <a href="%s"
                 style="
                   display: inline-block;
                   padding: 12px 24px;
                   font-size: 16px;
                   color: #ffffff;
                   background-color: #28a745;
                   text-decoration: none;
                   border-radius: 4px;
                 ">
                Verify Account
              </a>
            </p>
            <p>If that button doesn’t work, copy and paste this link into your browser:</p>
            <p><a href="%s">%s</a></p>
            <p>If you did not register, please ignore this email.</p>
            <p>Thanks,<br/>The E-Commerce Team</p>
            """.formatted(name, verifyLink, verifyLink, verifyLink);

        sendHtmlEmail(toEmail, subject, html);
    }


    public void sendReactivationEmail(String toEmail, String name, String token) {
        String subject = "Reset Your Password";
        String resetLink = frontendBaseUrl + token;

        String html = """
            <p>Dear %s,</p>
            <p>Click the button below to reset your password:</p>
            <p style="text-align: center;">
              <a href="%s"
                 style="
                   display: inline-block;
                   padding: 12px 24px;
                   font-size: 16px;
                   color: #ffffff;
                   background-color: #007bff;
                   text-decoration: none;
                   border-radius: 4px;
                 ">
                Reset Password
              </a>
            </p>
            <p>If that button doesn’t work, copy and paste this link into your browser:</p>
            <p><a href="%s">%s</a></p>
            <p>If you did not request a password reset, please ignore this email.</p>
            <p>Thanks,<br/>The E-Commerce Team</p>
            """.formatted(name, resetLink, resetLink, resetLink);

        sendHtmlEmail(toEmail, subject, html);
    }


    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);    // true = isHtml
            mailSender.send(mime);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }
}
