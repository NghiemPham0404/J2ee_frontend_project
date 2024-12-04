package com.example.j2ee_frontend_test.services;

import com.example.j2ee_frontend_test.DTOs.CertificateContext;
import com.example.j2ee_frontend_test.models.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(Email email) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email.getToEmail());
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody());
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendCertificate(Email email, CertificateContext certificateContext) {
        try {
            // Create email message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Prepare the data for Thymeleaf template
            Context context = new Context();
            context.setVariable("fullname", certificateContext.getFullName());
            context.setVariable("event", certificateContext.getEvent());
            context.setVariable("donation", certificateContext.getDonation());
            context.setVariable("time", certificateContext.getTime());

            String htmlContent = templateEngine.process("certification_template", context);
            helper.setText(htmlContent, true);

            // Attach the image as an inline resource
            Resource resource = new ClassPathResource("/static/img/v1_2.png");
            helper.addInline("v1_2", resource);

            helper.setTo(email.getToEmail());
            helper.setSubject(email.getSubject());

            // Send email
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }
}
