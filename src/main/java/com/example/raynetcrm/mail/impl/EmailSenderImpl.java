package com.example.raynetcrm.mail.impl;

import com.example.raynetcrm.mail.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;
    @Value("${rcrm.api.email.from}")
    private String from;
    @Value("${rcrm.api.email.to}")
    private String to;

    public void sendEmail(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
