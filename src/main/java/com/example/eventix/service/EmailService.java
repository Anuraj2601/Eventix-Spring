package com.example.eventix.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.example.eventix.entity.Meeting;
import com.example.eventix.repository.MeetingRepo;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage; // Using SimpleMailMessage instead of MimeMessageHelper

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MeetingRepo meetingRepo;

    private String qrCodeDirectory = "src/main/resources/static/qr-codes/";

    // Send the email with the QR code attached
    public void sendQrCodeEmail(String userEmail, String qrCodeData, String meetingId, String meetingName) throws Exception {
        try {
            // Generate the QR code image
            BufferedImage qrCodeImage = generateQrCodeImage(qrCodeData);  // Directly use qrCodeData

            // Store the QR code image as a PNG file
            File qrCodeFile = storeQrCodeImageAsFile(qrCodeImage, qrCodeData);

            // Send the email with QR code attachment
            sendEmailWithAttachment(
                    userEmail,
                    "Invitation to " + meetingName,
                    "Hello,\n\nYou are invited to the  " + meetingName + ".\n\nPlease find your QR code for the meeting attached.\n\nThank you!",
                    qrCodeFile
            );
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new Exception("Error sending QR code email: " + e.getMessage());
        }
    }


    // Helper method to generate a QR code image (BufferedImage)
    private BufferedImage generateQrCodeImage(String qrCodeData) {
        try {
            MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.MARGIN, 1);  // Small margin
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 300, 300, hintMap);

            int width = bitMatrix.getWidth();
            BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, width);

            graphics.setColor(Color.BLACK);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            return image;
        } catch (WriterException e) {
            throw new RuntimeException("Error generating QR code", e);
        }
    }

    // Helper method to store the generated QR code image as a PNG file
    private File storeQrCodeImageAsFile(BufferedImage qrCodeImage, String qrCodeData) throws IOException {
        // Create a file name based on the QR code data (you can customize this)
        String fileName = qrCodeData.replaceAll("[^a-zA-Z0-9]", "_") + ".png";  // Replace non-alphanumeric chars
        File qrCodeFile = new File(qrCodeDirectory, fileName);

        // Ensure the directory exists, and create it if not
        if (!new File(qrCodeDirectory).exists()) {
            new File(qrCodeDirectory).mkdirs();
        }

        // Write the image to the file
        ImageIO.write(qrCodeImage, "PNG", qrCodeFile);

        return qrCodeFile;
    }

    // Send email with the attachment using the simpler way (via MimeMessageHelper)
    public void sendEmailWithAttachment(String toEmail, String subject, String body, File attachment) throws MailException {
        try {
            // MimeMessageHelper is still required for email attachment handling
            MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true); // true indicates multipart message

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(attachment.getName(), attachment);

            mailSender.send(helper.getMimeMessage());
            System.out.println("Email sent successfully with QR code attachment.");
        } catch (Exception e) {
            throw new MailException("Error while sending email with attachment: " + e.getMessage()) {};
        }
    }

    // Send event approval email (without using MimeMessageHelper)
    public void sendEventApprovalEmail(String recipientEmail, String eventName, String eventDetails, String clubName) {
        try {
            // Create a simple email message for event approval
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Event Approved: " + eventName);
            message.setText(buildApprovalEmailBody(eventName, eventDetails, clubName));

            // Send the email
            mailSender.send(message);
            System.out.println("Approval email sent to " + recipientEmail);
        } catch (Exception e) {
            System.err.println("Failed to send approval email: " + e.getMessage());
        }
    }

    private String buildApprovalEmailBody(String eventName, String eventDetails, String clubName) {
        return "<html>" +
                "<body>" +
                "<h2>Event Approved!</h2>" +
                "<p>Dear Sir/Madam,</p>" +
                "<p>The event <strong>" + eventName + "</strong> from <strong>" + clubName + "</strong> has been approved.</p>" +
                "<p>Details:</p>" +
                "<ul>" +
                "<li>" + eventDetails + "</li>" +
                "</ul>" +
                "<p>Thank you,</p>" +
                "<p>Eventix Team</p>" +
                "</body>" +
                "</html>";
    }

    public void sendMeetingCode(int meetingId, String recipientEmail) throws Exception {
        // Fetch meeting details
        Optional<Meeting> meetingOptional = meetingRepo.findById(meetingId);
        if (meetingOptional.isEmpty()) {
            throw new Exception("Meeting not found for ID: " + meetingId);
        }
        Meeting meeting = meetingOptional.get();

        // Ensure the meeting is an online meeting and has a code
        if (!"ONLINE".equalsIgnoreCase(meeting.getMeetingType())) {
            throw new Exception("This meeting is not an online meeting.");
        }
        String meetingCode = meeting.getMeetingLink(); // Assuming meeting entity has a field `meetingCode`
        if (meetingCode == null || meetingCode.isBlank()) {
            throw new Exception("Meeting code is not generated for this meeting.");
        }

        // Compose email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Online Meeting Code - " + meeting.getMeeting_name());
        helper.setText(
                "Dear User,\n\n" +
                        "Please find the details of your online meeting below:\n\n" +
                        "Meeting Name: " + meeting.getMeeting_name() + "\n" +
                        "Date: " + meeting.getDate() + "\n" +
                        "Time: " + meeting.getTime() + "\n\n" +
                        "Meeting Code/Link: " + meetingCode + "\n\n" +
                        "Thank you.",
                false
        );

        // Send email
        mailSender.send(message);
    }
}
