package com.example.eventix.service;

import com.example.eventix.dto.EventDTO;
import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Event;
import com.example.eventix.entity.Meeting;
import com.example.eventix.repository.EventRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveEvent(EventDTO eventDTO) {
        try{
            if(eventRepo.existsById(eventDTO.getEvent_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event already exists");
                responseDTO.setContent(eventDTO);

            }else{
                Event savedEvent = eventRepo.save(modelMapper.map(eventDTO, Event.class));
                EventDTO savedEventDTO = modelMapper.map(savedEvent, EventDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Saved Successfully");
                responseDTO.setContent(savedEventDTO);
            }

            return responseDTO;

        }catch(Exception e){

            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;

        }
    }

    public ResponseDTO getAllEvents(){
        try{
            List<Event> eventsList = eventRepo.findAll();
            if(!eventsList.isEmpty()){
                List<EventDTO> eventDTOList = modelMapper.map(eventsList, new TypeToken<List<EventDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Events Successfully");
                responseDTO.setContent(eventDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Events Found");
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

    public ResponseDTO getEvent(int event_id){
        try{
            if(eventRepo.existsById(event_id)){
                Event event = eventRepo.findById(event_id).orElse(null);
                EventDTO eventDTO =  modelMapper.map(event, EventDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Retrieved Successfully");
                responseDTO.setContent(eventDTO);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Found");
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

    public ResponseDTO updateEvent(EventDTO eventDTO){
        try{
            if(eventRepo.existsById(eventDTO.getEvent_id())){
                Event updatedEvent =  eventRepo.save(modelMapper.map(eventDTO, Event.class));
                EventDTO updatedEventDTO = modelMapper.map(updatedEvent, EventDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Updated Successfully");
                responseDTO.setContent(updatedEventDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Found");
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

    public ResponseDTO deleteEvent(int event_id){
        try{
            if(eventRepo.existsById(event_id)){
                eventRepo.deleteById(event_id);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Deleted Successfully");
                responseDTO.setContent(null);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Found");
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

//    public String uploadPhoto(int event_id, MultipartFile file) {
//        log.info("Saving picture for user ID: {}", event_id);
//        ResponseDTO responseDTO = getEvent(event_id);
//        String imageUrl = photoFunction.apply(event_id, file);
//        responseDTO.setContent(imageUrl);
//        eventRepo.save(modelMapper.map(studentDTO, Student.class));
//        return photoUrl;
//
//    }
}
