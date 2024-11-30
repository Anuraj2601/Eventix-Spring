package com.example.eventix.service;

import com.example.eventix.repository.UsersRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.Map;

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

    public void sendQrCodeEmail(String toEmail, Map<Long, String> qrCodeDataUrls, Long meetingId) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Set email subject and recipient
        helper.setTo(toEmail);
        helper.setSubject("Your QR Code for Meeting ID: " + meetingId);

        // Prepare email content
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html>")
                .append("<body>")
                .append("<h3>Dear User,</h3>")
                .append("<p>Please find below the QR code(s) for Meeting ID: ").append(meetingId).append(".</p>");

        for (Map.Entry<Long, String> entry : qrCodeDataUrls.entrySet()) {
            Long participantId = entry.getKey();
            String qrCodeDataUrl = entry.getValue();

            // Validate QR Code Data URL
            if (qrCodeDataUrl == null || !qrCodeDataUrl.contains(",")) {
                throw new IllegalArgumentException("Invalid QR code data format for participant ID: " + participantId);
            }

            // Extract Base64 Data
            String base64Data = qrCodeDataUrl.split(",")[1];

            // Decode Base64 and Save QR Code to File
            String filePath = "path/to/temp/qrcode_" + participantId + ".png";
            File qrCodeFile = new File(filePath);

            try (OutputStream os = new FileOutputStream(qrCodeFile)) {
                os.write(Base64.getDecoder().decode(base64Data));
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error writing QR code to file for participant ID: " + participantId, e);
            }

            // Add QR Code to Email Content
            String contentId = "qrCode" + participantId; // Unique Content-ID
            emailContent.append("<p>Participant ID: ").append(participantId).append("</p>")
                    .append("<img src='cid:").append(contentId).append("' alt='QR Code' style='max-width: 300px; height: auto;' />")
                    .append("<hr>");

            // Attach the QR Code to the email as an inline image
            helper.addInline(contentId, qrCodeFile);
        }

        emailContent.append("<p>Thank you!</p>")
                .append("</body>")
                .append("</html>");

        // Set email body with HTML content
        helper.setText(emailContent.toString(), true);

        // Send the email
        mailSender.send(mimeMessage);

        // Clean up temporary files
        for (Long participantId : qrCodeDataUrls.keySet()) {
            String filePath = "path/to/temp/qrcode_" + participantId + ".png";
            File qrCodeFile = new File(filePath);
            if (qrCodeFile.exists()) {
                qrCodeFile.delete();
            }
            }
    }
}
