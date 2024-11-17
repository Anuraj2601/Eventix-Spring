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
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class MeetingService {

    private static final String VIDEO_SDK_API_URL = "https://api.videosdk.live/v2/rooms";
    private static final String VIDEO_SDK_API_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJjYTM3NmJiMi05MTU2LTQyN2UtOTJmNC0yZGJiOWM1MGQ1MTQiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTczMTc2NDU0MiwiZXhwIjoxNzYzMzAwNTQyfQ.I616v8R7lhwqv70muOQXvU6oRxzri-eWC605uh9ladc";

    @Autowired
    private MeetingRepo meetingRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private ClubsRepo clubsRepo;

    @Autowired
    private QRCodeService qrCodeService; // Autowire QR code service

    public ResponseDTO saveMeeting(MeetingDTO meetingDTO) {
        try {
            if (meetingRepo.existsById(meetingDTO.getMeeting_id())) {
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Meeting already exists");
                responseDTO.setContent(meetingDTO);
            } else {
                // Retrieve the club entity
                Clubs club = clubsRepo.findById(meetingDTO.getClubId())
                        .orElseThrow(() -> new RuntimeException("Club not found"));

                // Map DTO to entity
                Meeting meeting = modelMapper.map(meetingDTO, Meeting.class);
                meeting.setClubs(club);

                // Handle physical and online meeting types
                if ("PHYSICAL".equalsIgnoreCase(meetingDTO.getMeetingType())) {
                    // Generate QR code for physical meeting
                    // Generate a unique file name for the QR code based on meeting ID
                    String fileName = "meeting-" + meetingDTO.getMeetingId();
                    String qrCodeUrl = qrCodeService.generateQRCode("Meeting ID: " + meetingDTO.getMeetingId(), 200, 200, fileName);
                    meetingDTO.setQrCodeUrl(qrCodeUrl); // Set the generated QR code URL
                } else if ("ONLINE".equalsIgnoreCase(meetingDTO.getMeetingType())) {
                    // Generate VideoSDK link for online meeting
                    String meetingLink = createVideoSdkMeetingLink();
                    meetingDTO.setMeetingLink(meetingLink); // Set the generated VideoSDK link
                }


                // Save meeting entity
                Meeting savedMeeting = meetingRepo.save(meeting);
                MeetingDTO savedMeetingDTO = modelMapper.map(savedMeeting, MeetingDTO.class);

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Saved Successfully");
                responseDTO.setContent(savedMeetingDTO);
            }

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
                List<MeetingDTO> meetingDTOList = modelMapper.map(meetingsList, new TypeToken<List<MeetingDTO>>() {}.getType());
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

    public ResponseDTO updateMeeting(int meeting_id, MeetingDTO meetingDTO) {
        try {
            if (meetingRepo.existsById(meeting_id)) {
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
}
