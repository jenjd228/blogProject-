package com.blog.spring.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class MailConfig {

    private final Logger logger = Logger.getLogger(MailConfig.class);

    @Value("${spring.mail.host}")
    private String springMailHost;

    @Value("${spring.mail.port}")
    private int springMailPort;

    @Value("${spring.mail.username}")
    private String springMailUsername;

    @Value("${spring.mail.password}")
    private String springMailPassword;

    @Value("${mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String springMailPropertiesMailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String springMailPropertiesMailSmtpStarttlsEnable;

    @Bean
    public MailSender mainSender() {
        logger.info("Mail " + springMailUsername + "mail.smtp.starttls.enable : " + springMailPropertiesMailSmtpStarttlsEnable + "mail.smtp.auth: " + springMailPropertiesMailSmtpAuth);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(springMailHost);
        mailSender.setPort(springMailPort);

        mailSender.setUsername(springMailUsername);
        mailSender.setPassword(springMailPassword);

        Properties properties = mailSender.getJavaMailProperties();

        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", springMailPropertiesMailSmtpAuth);
        properties.put("mail.smtp.starttls.enable", springMailPropertiesMailSmtpStarttlsEnable);
        properties.put("mail.debug", "true");
        properties.put("mail.from.email", springMailUsername);

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(springMailUsername, springMailPassword);
            }
        };

        Session session = Session.getInstance(properties, auth);
        mailSender.setSession(session);
        return mailSender;
    }

}
