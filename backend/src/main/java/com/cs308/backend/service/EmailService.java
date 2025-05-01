package com.cs308.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvoiceEmail(String to, String subject, String text, byte[] pdfContent, String invoiceNumber) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // Attach PDF
            StringBuilder builder = new StringBuilder();
            builder.append("invoice-").append(invoiceNumber).append(".pdf");
            String invoiceFilename = builder.toString();
            helper.addAttachment(invoiceFilename,
                    new ByteArrayResource(pdfContent));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }

    public void sendNotificationEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }
}