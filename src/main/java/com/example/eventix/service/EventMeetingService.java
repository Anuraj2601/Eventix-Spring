package com.example.eventix.service;

import com.example.eventix.dto.BudgetDTO;
import com.example.eventix.dto.EventMeetingDTO;
import com.example.eventix.dto.EventRegistrationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventMeeting;
import com.example.eventix.repository.EventMeetingRepo;
import com.example.eventix.repository.EventRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EventMeetingService {

    @Autowired
    private EventMeetingRepo eventMeetingRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private EventRepo eventRepo;

    public ResponseDTO saveEventMeeting(EventMeetingDTO eventMeetingDTO){
        try{
            if(eventMeetingRepo.existsById(eventMeetingDTO.getE_meeting_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Meeting already exists");
                responseDTO.setContent(eventMeetingDTO);

            }else{

                EventMeeting eventMeeting = modelMapper.map(eventMeetingDTO, EventMeeting.class);

                eventMeeting.setEvent(eventRepo.findById(eventMeetingDTO.getEvent_id()).orElse(null));


                EventMeeting savedEventMeeting = eventMeetingRepo.save(eventMeeting);
                EventMeetingDTO savedEventMeetingDTO = modelMapper.map(savedEventMeeting, EventMeetingDTO.class);


                savedEventMeetingDTO.setEvent_id(savedEventMeeting.getEvent().getEvent_id());


                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Meeting Saved Successfully");
                responseDTO.setContent(savedEventMeetingDTO);



            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }


    }

    public ResponseDTO getAllEventMeetings(){

        try{
            List<EventMeeting> eventMeetingsList = eventMeetingRepo.findAll();
            if(!eventMeetingsList.isEmpty()){
                List<EventRegistrationDTO> eventMeetingDTOList = modelMapper.map(eventMeetingsList, new TypeToken<List<EventMeetingDTO>>(){}.getType());

                // Manually set the user id and Event_reg_id for each EventRegistrationDTO
//                for (int i = 0; i < eventMeetingsList.size(); i++) {
//                    eventRegistrationDTOList.get(i).setUser_id(eventMeetingsList.get(i).getStudent().getId());
//                    eventRegistrationDTOList.get(i).setEReg_id(eventMeetingsList.get(i).getE_reg_id());
//                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All event Meetings Successfully");
                responseDTO.setContent(eventMeetingDTOList);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Meeting Found");
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

    public ResponseDTO getEventMeeting(int eventMeetingId){

        try{
            if(eventMeetingRepo.existsById(eventMeetingId)){
                EventMeeting eventMeeting = eventMeetingRepo.findById(eventMeetingId).orElse(null);
                EventMeetingDTO eventMeetingDTO =  modelMapper.map(eventMeeting, EventMeetingDTO.class);

                // Manually set user_id
//                eventMeetingDTO.setUser_id(eventMeeting.getStudent().getId());
//                eventMeetingDTO.setEReg_id(eventMeeting.getE_reg_id());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Meeting Retrieved Successfully");
                responseDTO.setContent(eventMeetingDTO);

            }else{

                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Meeting found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        }catch (Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }

    }

    public ResponseDTO updateEventMeeting(int eventMeetingId, EventMeetingDTO eventMeetingDTO){
        try{
            if(eventMeetingRepo.existsById(eventMeetingId)){
                EventMeeting updatedEventMeeting =  eventMeetingRepo.save(modelMapper.map(eventMeetingDTO, EventMeeting.class));
                EventMeetingDTO updatedEventMeetingDTO = modelMapper.map(updatedEventMeeting, EventMeetingDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Meeting Updated Successfully");
                responseDTO.setContent(updatedEventMeetingDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Meeting Found");
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


    public ResponseDTO deleteEventMeeting(int eventMeetingId){
        try{
            if(eventMeetingRepo.existsById(eventMeetingId)){
                eventMeetingRepo.deleteById(eventMeetingId);
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Meeting deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Meeting Found");
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
