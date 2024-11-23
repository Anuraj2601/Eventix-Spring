package com.example.eventix.service;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.dto.EventAnnouncementDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventAnnouncement;
import com.example.eventix.repository.EventAnnouncementRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EventAnnouncementService {

    @Autowired
    private EventAnnouncementRepo eventAnnouncementRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;


    public ResponseDTO saveEventAnnouncement(EventAnnouncementDTO eventAnnouncementDTO){
        try{
            if(eventAnnouncementRepo.existsById(eventAnnouncementDTO.getE_announcement_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Announcement already exists");
                responseDTO.setContent(eventAnnouncementDTO);

            }else{
                EventAnnouncement savedEventAnnouncement =  eventAnnouncementRepo.save(modelMapper.map(eventAnnouncementDTO, EventAnnouncement.class));
                EventAnnouncementDTO savedEventAnnouncementDTO = modelMapper.map(savedEventAnnouncement, EventAnnouncementDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Announcement Saved Successfully");
                responseDTO.setContent(savedEventAnnouncementDTO);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }


    }

    public ResponseDTO getAllEventAnnouncements(){

        try{
            List<EventAnnouncement> eventAnnouncementsList = eventAnnouncementRepo.findAll();
            if(!eventAnnouncementsList.isEmpty()){
                List<EventAnnouncementDTO> eventAnnouncementDTOList = modelMapper.map(eventAnnouncementsList, new TypeToken<List<EventAnnouncementDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Event Announcements Successfully");
                responseDTO.setContent(eventAnnouncementDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Announcements Found");
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

    public ResponseDTO getEventAnnouncement(int eventAnnouncementId){

        try{
            if(eventAnnouncementRepo.existsById(eventAnnouncementId)){
                EventAnnouncement eventAnnouncement = eventAnnouncementRepo.findById(eventAnnouncementId).orElse(null);
                EventAnnouncementDTO eventAnnouncementDTO =  modelMapper.map(eventAnnouncement, EventAnnouncementDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Announcement Retrieved Successfully");
                responseDTO.setContent(eventAnnouncementDTO);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Announcement found");
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

    public ResponseDTO updateEventAnnouncement(int eventAnnouncementId, EventAnnouncementDTO eventAnnouncementDTO){
        try{
            if(eventAnnouncementRepo.existsById(eventAnnouncementId)){
                EventAnnouncement updatedEventAnnouncement =  eventAnnouncementRepo.save(modelMapper.map(eventAnnouncementDTO, EventAnnouncement.class));
                EventAnnouncementDTO updatedEventAnnouncementDTO = modelMapper.map(updatedEventAnnouncement, EventAnnouncementDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Announcement Updated Successfully");
                responseDTO.setContent(updatedEventAnnouncementDTO);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Announcement Found");
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

    public ResponseDTO deleteEventAnnouncement(int eventAnnouncementId){
        try{
            if(eventAnnouncementRepo.existsById(eventAnnouncementId)){
                eventAnnouncementRepo.deleteById(eventAnnouncementId);
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Announcement deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Announcement Found");
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
