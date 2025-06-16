package com.academy.e_commerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    public void sendVerificationEmail(String toEmail, String name, String token) {
        String subject = "Verify Your Email";
        String verificationUrl = baseUrl + "/users/verify?token=" + token;

        String body = """
            Dear %s,

            Please click the following link to verify your account:
            %s

            If you did not register, please ignore this email.
            """.formatted(name, verificationUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


    public void sendReactivationEmail(String toEmail, String token) {
        String subject = "Reactivate Your Account";

        String resetLink = baseUrl + "users/reset-password?token=" + token;

        String body = "Please click the following link to reset your password: " + resetLink;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }




}
