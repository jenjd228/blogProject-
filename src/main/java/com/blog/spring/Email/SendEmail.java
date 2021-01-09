package com.blog.spring.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {

    @Value("${spring.mail.username}")
    private String springMailUsername;

    @Autowired
    private MailSender mailSender;

    public void sendEmail(String message, String toEmail){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom(springMailUsername);
        msg.setSubject("Password restore");
        msg.setText(message);
        mailSender.send(msg);
    }
}