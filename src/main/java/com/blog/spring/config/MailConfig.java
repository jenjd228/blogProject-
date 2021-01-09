package com.blog.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String springMailHost;

    @Value("${spring.mail.port}")
    private int springMailPort;

    @Value("${spring.mail.username}")
    private String springMailUsername;

    @Value("${spring.mail.password}")
    private String springMailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String springMailPropertiesMailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String springMailPropertiesMailSmtpStarttlsEnable;

    @Bean
    public MailSender mainSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(springMailHost);
        mailSender.setPort(springMailPort);
        mailSender.setUsername(springMailUsername);
        mailSender.setPassword(springMailPassword);

        Properties properties = new Properties();

        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth",springMailPropertiesMailSmtpAuth);
        properties.put("mail.smtp.starttls.enable",springMailPropertiesMailSmtpStarttlsEnable);
        properties.put("mail.debug","true");

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

}
