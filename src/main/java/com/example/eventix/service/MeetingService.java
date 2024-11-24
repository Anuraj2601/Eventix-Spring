package com.example.eventix.service;

import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Clubs;
import com.example.eventix.entity.Meeting;
import com.example.eventix.repository.ClubsRepo;
import com.example.eventix.repository.MeetingRepo;
import com.example.eventix.util.VarList;
import com.example.eventix.service.QRCodeService; // Add QR code service
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MeetingService {

    private static final String VIDEO_SDK_API_URL = "https://api.videosdk.live/v2/rooms";
    private static final String VIDEO_SDK_API_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJjYTM3NmJiMi05MTU2LTQyN2UtOTJmNC0yZGJiOWM1MGQ1MTQiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTczMTc2NDU0MiwiZXhwIjoxNzYzMzAwNTQyfQ.I616v8R7lhwqv70muOQXvU6oRxzri-eWC605uh9ladc";

    @Autowired
    private MeetingRepo meetingRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private ClubsRepo clubsRepo;

    @Autowired
    private QRCodeService qrCodeService; // Autowire QR code service

    @Autowired
    private EmailService emailService;

    @Value("${qr.code.directory}")
    private String qrCodeDirectory;

    public ResponseDTO saveMeeting(MeetingDTO meetingDTO) {
        try {
            // Check if the meeting already exists
            if (meetingRepo.existsById(meetingDTO.getMeeting_id())) {
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Meeting already exists");
                responseDTO.setContent(meetingDTO);
                return responseDTO;
            }

            // Get the club by ID
            Clubs club = clubsRepo.findById(meetingDTO.getClub_id())
                    .orElseThrow(() -> new RuntimeException("Club not found"));

            // Map DTO to entity
            Meeting meeting = modelMapper.map(meetingDTO, Meeting.class);
            meeting.setClubs(club);

            // Handle meeting type (QR code for physical, meeting link for online)
            if ("PHYSICAL".equalsIgnoreCase(meetingDTO.getMeetingType())) {
                String qrCodeUrl = qrCodeService.generateQRCode("Meeting ID: " + meetingDTO.getMeetingId(), 200, 200, "meeting-" + meetingDTO.getMeetingId());
                meetingDTO.setQrCodeUrl(qrCodeUrl);
            } else if ("ONLINE".equalsIgnoreCase(meetingDTO.getMeetingType())) {
                String meetingLink = createVideoSdkMeetingLink();
                meetingDTO.setMeetingLink(meetingLink);
            }

            // Save meeting entity
            Meeting savedMeeting = meetingRepo.save(meeting);

            // After the save, you can now retrieve the meeting_id from the saved entity
            int meetingId = savedMeeting.getMeeting_id();  // The meeting_id is auto-generated after save

            // Now call the stored procedure with the saved meeting ID
            entityManager.createNativeQuery("CALL InsertMeetingParticipants(:meetingId, :clubId, :participantType)")
                    .setParameter("meetingId", meetingId) // Use the meetingId from the saved entity
                    .setParameter("clubId", meetingDTO.getClub_id()) // Directly fetch from DTO
                    .setParameter("participantType", meetingDTO.getParticipant_type().name()) // Use enum name as String
                    .executeUpdate();

            // Map saved entity back to DTO
            MeetingDTO savedMeetingDTO = modelMapper.map(savedMeeting, MeetingDTO.class);

            responseDTO.setStatusCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Meeting Saved Successfully");
            responseDTO.setContent(savedMeetingDTO);
            return responseDTO;

        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    public ResponseDTO getAllMeetings() {
        try {
            List<Meeting> meetingsList = meetingRepo.findAll();
            if (!meetingsList.isEmpty()) {
                List<MeetingDTO> meetingDTOList = meetingsList.stream().map(meeting -> {
                    MeetingDTO meetingDTO = modelMapper.map(meeting, MeetingDTO.class);
                    meetingDTO.setClub_id(meeting.getClubs().getClub_id()); // Map the club_id explicitly
                    return meetingDTO;
                }).toList();

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Meetings Successfully");
                responseDTO.setContent(meetingDTOList);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meetings Found");
                responseDTO.setContent(null);
            }

            return responseDTO;
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }


    public ResponseDTO getMeeting(int meeting_id) {
        try {
            if (meetingRepo.existsById(meeting_id)) {
                Meeting meeting = meetingRepo.findById(meeting_id).orElse(null);
                MeetingDTO meetingDTO = modelMapper.map(meeting, MeetingDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Retrieved Successfully");
                responseDTO.setContent(meetingDTO);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meeting Found");
                responseDTO.setContent(null);
            }

            return responseDTO;
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    public ResponseDTO updateMeeting(int meetingId, MeetingDTO meetingDTO) {
        try {
            if (meetingRepo.existsById(meetingId)) {
                Meeting updatedMeeting = meetingRepo.save(modelMapper.map(meetingDTO, Meeting.class));
                MeetingDTO updatedMeetingDTO = modelMapper.map(updatedMeeting, MeetingDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Updated Successfully");
                responseDTO.setContent(updatedMeetingDTO);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meeting Found");
                responseDTO.setContent(null);
            }
            return responseDTO;
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }
    public ResponseDTO deleteMeeting(int meeting_id) {
        try {
            if (meetingRepo.existsById(meeting_id)) {
                meetingRepo.deleteById(meeting_id);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Deleted Successfully");
                responseDTO.setContent(null);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meeting Found");
                responseDTO.setContent(null);
            }

            return responseDTO;
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e);
            return responseDTO;
        }
    }

    // A method to simulate creating a VideoSDK meeting link for online meetings
    public String createVideoSdkMeetingLink() {
        try {
            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VIDEO_SDK_API_URL))
                    .header("Authorization", VIDEO_SDK_API_TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{}")) // Empty JSON body
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response is successful (HTTP status 200)
            if (response.statusCode() == 200) {
                // Extract the meeting link (roomId) from the response body
                String responseBody = response.body();
                System.out.println("VideoSDK Response: " + responseBody);

                // Assuming the response JSON contains a field "roomId"
                // e.g., {"roomId": "abc123"}
                return parseRoomIdFromResponse(responseBody);
            } else {
                System.err.println("Failed to create VideoSDK meeting. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return a default message if the request fails
        return null;
    }

    private String parseRoomIdFromResponse(String responseBody) {
        try {
            // Use a JSON library like org.json, Jackson, or Gson to parse the response
            org.json.JSONObject jsonObject = new org.json.JSONObject(responseBody);
            return jsonObject.getString("roomId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseDTO joinOnlineMeeting(int meetingId) {
        try {
            Optional<Meeting> meetingOpt = meetingRepo.findById(meetingId);
            if (meetingOpt.isPresent()) {
                Meeting meeting = meetingOpt.get();
                if (!"ONLINE".equalsIgnoreCase(meeting.getMeetingType())) {
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("This is not an online meeting");
                    responseDTO.setContent(null);
                    return responseDTO;
                }

                // Generate or fetch the meeting token/link from VideoSDK
                RestTemplate restTemplate = new RestTemplate();
                String meetingLink;

                if (meeting.getMeetingLink() == null || meeting.getMeetingLink().isEmpty()) {
                    // Generate a new meeting link if not already created
                    var request = new org.springframework.http.HttpEntity<>(null, createHeaders());
                    var response = restTemplate.postForEntity(VIDEO_SDK_API_URL, request, String.class);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        // Assuming response body contains the meeting ID or link
                        meetingLink = response.getBody(); // Replace this with correct parsing logic
                        meeting.setMeetingLink(meetingLink);
                        meetingRepo.save(meeting); // Update DB with the new meeting link
                    } else {
                        responseDTO.setStatusCode(VarList.RSP_ERROR);
                        responseDTO.setMessage("Failed to create meeting link");
                        responseDTO.setContent(null);
                        return responseDTO;
                    }
                } else {
                    // Use the existing meeting link
                    meetingLink = meeting.getMeetingLink();
                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Join online meeting using the provided link");
                responseDTO.setContent(meetingLink);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Meeting not found");
                responseDTO.setContent(null);
            }

            return responseDTO;
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    /**
     * Helper method to create HTTP headers with the API key
     */
    private org.springframework.http.HttpHeaders createHeaders() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + VIDEO_SDK_API_TOKEN);
        return headers;
    }

    public void sendQrCodeToUser(int meetingId, String userEmail) throws Exception {
        // Fetch the meeting
        Meeting meeting = meetingRepo.findById(meetingId)
                .orElseThrow(() -> new Exception("Meeting not found with ID: " + meetingId));

        // Ensure the QR code URL exists
        if (meeting.getQrCodeUrl() == null || meeting.getQrCodeUrl().isEmpty()) {
            throw new Exception("QR Code not available for this meeting.");
        }

        // Construct the file path to the QR code
        String fileName = meeting.getQrCodeUrl().substring(meeting.getQrCodeUrl().lastIndexOf("/") + 1);
        File qrCodeFile = new File(qrCodeDirectory, fileName);

        // Ensure the QR code file exists
        if (!qrCodeFile.exists()) {
            throw new Exception("QR Code file not found for meeting: " + meetingId);
        }

        // Send the email with the QR code as an attachment
        emailService.sendEmailWithAttachment(
                userEmail,
                "Your Meeting QR Code",
                "Hello,\n\nPlease find the QR code for your meeting attached.\n\nThank you!",
                qrCodeFile
        );
    }

    public void sendMeetingCodeToUser(int meetingId, String email) throws Exception {
        Meeting meeting = meetingRepo.findById(meetingId)
                .orElseThrow(() -> new Exception("Meeting not found with ID: " + meetingId));

        if (meeting.getMeetingLink() == null || meeting.getMeetingLink().isEmpty()) {
            throw new Exception("Meeting Code not available for this meeting.");
        }

        String meetingCode = meeting.getMeetingLink().substring(meeting.getMeetingLink().lastIndexOf("/") + 1);

        String emailBody = String.format(
                "Hello,\n\nHere is the meeting code: %s\n\nThank you!",
                meetingCode
        );

        emailService.sendEmailWithAttachment1(
                email,
                "Your Online Meeting Code",
                emailBody
        );
    }
}
