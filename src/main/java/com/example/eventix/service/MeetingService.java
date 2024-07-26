package com.example.eventix.service;


import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;

import com.example.eventix.entity.Meeting;
import com.example.eventix.repository.MeetingRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MeetingService {

    @Autowired
    private MeetingRepo meetingRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveMeeting(MeetingDTO meetingDTO) {
        try{
            if(meetingRepo.existsById(meetingDTO.getMeeting_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Meeting already exists");
                responseDTO.setContent(meetingDTO);

            }else{
                Meeting savedMeeting = meetingRepo.save(modelMapper.map(meetingDTO, Meeting.class));
                MeetingDTO savedMeetingDTO = modelMapper.map(savedMeeting, MeetingDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Saved Successfully");
                responseDTO.setContent(savedMeetingDTO);
            }

            return responseDTO;

        }catch(Exception e){

            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getAllMeetings(){
        try{
            List<Meeting> meetingsList = meetingRepo.findAll();
            if(!meetingsList.isEmpty()){
                List<MeetingDTO> meetingDTOList = modelMapper.map(meetingsList, new TypeToken<List<MeetingDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Meetings Successfully");
                responseDTO.setContent(meetingDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meetings Found");
                responseDTO.setContent(null);

            }

            return responseDTO;


        }catch(Exception e){

            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getMeeting(int meeting_id){
        try{
            if(meetingRepo.existsById(meeting_id)){
                Meeting meeting = meetingRepo.findById(meeting_id).orElse(null);
                MeetingDTO meetingDTO =  modelMapper.map(meeting, MeetingDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Retrieved Successfully");
                responseDTO.setContent(meetingDTO);

            }else{
               responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
               responseDTO.setMessage("No Meeting Found");
               responseDTO.setContent(null);


            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO updateMeeting(MeetingDTO meetingDTO){
        try{
            if(meetingRepo.existsById(meetingDTO.getMeeting_id())){
                Meeting updatedMeeting =  meetingRepo.save(modelMapper.map(meetingDTO, Meeting.class));
                MeetingDTO updatedMeetingDTO = modelMapper.map(updatedMeeting, MeetingDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Updated Successfully");
                responseDTO.setContent(updatedMeetingDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meeting Found");
                responseDTO.setContent(null);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    public ResponseDTO deleteMeeting(int meeting_id){
        try{
            if(meetingRepo.existsById(meeting_id)){
                meetingRepo.deleteById(meeting_id);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Meeting Deleted Successfully");
                responseDTO.setContent(null);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Meeting Found");
                responseDTO.setContent(null);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(e);
            return responseDTO;
        }
    }
}
