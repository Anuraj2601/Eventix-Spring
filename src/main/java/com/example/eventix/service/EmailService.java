package com.example.eventix.service;

import com.example.eventix.repository.UsersRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsersRepo userRepository;

    public void sendEmailWithAttachment(String to, String subject, String text, File attachment) throws MessagingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Add attachment
        helper.addAttachment(attachment.getName(), attachment);

        mailSender.send(message);
    }

    public void sendEmailWithAttachment1(String email, String yourOnlineMeetingCode, String emailBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject(yourOnlineMeetingCode);
        helper.setText(emailBody);

        mailSender.send(message);
    }

    public void sendQRCodeEmail(String userId, String qrCodeImage) throws MessagingException {
        // Get user email by ID (logic omitted)
        String email = userRepository.getEmailByUserId(Integer.parseInt(userId));

        // Send email logic
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Your QR Code for the Meeting");
        helper.setText("Attached is your QR code for the meeting.\n\n" + qrCodeImage);

        mailSender.send(message);
    }
}
