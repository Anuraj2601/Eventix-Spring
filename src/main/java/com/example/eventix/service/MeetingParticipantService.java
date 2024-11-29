package com.example.eventix.service;

import com.example.eventix.dto.MeetingParticipantDTO;
import com.example.eventix.entity.MeetingParticipant;
import com.example.eventix.repository.MeetingParticipantRepository;
import com.example.eventix.repository.UsersRepo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingParticipantService {

    @Autowired
    private MeetingParticipantRepository participantRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsersRepo userRepository;

    // Fetch all participants for a specific meeting
    public List<MeetingParticipantDTO> getAllParticipants(int meetingId) {
        List<MeetingParticipant> participants = participantRepository.findByMeetingId(meetingId);
        return participants.stream()
                .map(participant -> new MeetingParticipantDTO(
                        participant.getParticipantId(),
                        participant.getMeetingId(),
                        participant.getUserId(),
                        participant.getClubId(),
                        participant.getAttendance(),
                        participant.getQrCodeUser()))
                .collect(Collectors.toList());
    }

    // Update attendance for a specific user and meeting
    public String updateAttendance(int userId, int meetingId, int attendanceStatus) {
        MeetingParticipant participant = participantRepository.findByUserIdAndMeetingId(userId, meetingId);
        if (participant != null) {
            participant.setAttendance(attendanceStatus);
            participantRepository.save(participant);  // Update participant
            return "Attendance updated successfully";
        } else {
            return "Participant not found";
        }
    }

    public String generateQRCodeImage(String data) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage qrImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(qrImage, "png", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(qrCodeBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendQRCodeEmail(String userId, String qrCodeImage) throws Exception {
        String email = userRepository.getEmailByUserId(Integer.parseInt(userId));
        if (email == null || email.isEmpty()) {
            throw new Exception("Email not found for user ID: " + userId);
        }

        // Compose email with embedded QR code
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Your QR Code for the Meeting");
        helper.setText("<html><body>"
                + "<p>Attached is your QR code for the meeting.</p>"
                + "<img src='data:image/png;base64," + qrCodeImage + "' alt='QR Code' />"
                + "</body></html>", true);

        // Send the email
        mailSender.send(message);
    }
}
