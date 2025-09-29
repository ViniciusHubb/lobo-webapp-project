package com.lobobombeiros.usuariosservice.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Redefinição de Senha");
            String resetUrl = "http://localhost:4200/redefinir-senha?token=" + token;
            message.setText("Para redefinir sua senha, clique no link abaixo:\n" + resetUrl);

            logger.info("Enviando e-mail de redefinição de senha para {}", to);
            mailSender.send(message);
            logger.info("E-mail enviado com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail de redefinição de senha para {}: {}", to, e.getMessage());
        }
    }
}
