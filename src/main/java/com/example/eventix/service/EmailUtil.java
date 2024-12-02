package com.example.eventix.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        System.out.println("Sending OTP email to " + email);

        // Create a new MimeMessage to send the email
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // Helper for setting the email properties
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        // Set the recipient email
        mimeMessageHelper.setTo(email);

        // Set the subject of the email
        mimeMessageHelper.setSubject("Verify OTP");

        // Build the HTML content of the email
        String emailContent = """
        <div>
            <h3>Hello,</h3>
            <p>Your OTP is: <strong>%s</strong></p>
            <p>Please use this OTP to complete your verification process.</p>
            <p>If you did not request this, please ignore this email.</p>
            <p><a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">Click here to verify your account</a></p>
        </div>
    """.formatted(otp, email, otp);

        // Set the email content as HTML
        mimeMessageHelper.setText(emailContent, true);

        // Send the email
        javaMailSender.send(mimeMessage);
    }

}
