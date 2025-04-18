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
            helper.addAttachment("invoice-" + invoiceNumber + ".pdf",
                    new ByteArrayResource(pdfContent));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }
}