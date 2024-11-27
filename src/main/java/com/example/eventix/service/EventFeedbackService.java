package com.example.eventix.service;

import com.example.eventix.dto.EventFeedbackDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventFeedback;
import com.example.eventix.repository.EventFeedbackRepo;
import com.example.eventix.repository.EventRepo;
import com.example.eventix.repository.UsersRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EventFeedbackService {

    @Autowired
    private EventFeedbackRepo eventFeedbackRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;


    public ResponseDTO saveEventFeedback(EventFeedbackDTO eventFeedbackDTO){
        try{
            if(eventFeedbackRepo.existsById(eventFeedbackDTO.getE_feedback_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Feedback already exists");
                responseDTO.setContent(eventFeedbackDTO);

            }else{

                EventFeedback eventFeedback = modelMapper.map(eventFeedbackDTO, EventFeedback.class);

                eventFeedback.setStudent(usersRepo.findById(eventFeedbackDTO.getUser_id()).orElse(null));
                //eventFeedback.setEvent(eventRepo.findById(eventFeedbackDTO.getEvent_id()).orElse(null));


                EventFeedback savedEventFeedback = eventFeedbackRepo.save(eventFeedback);
                EventFeedbackDTO savedEventFeedbackDTO = modelMapper.map(savedEventFeedback, EventFeedbackDTO.class);

                // Manually set user_id
                savedEventFeedbackDTO.setUser_id(savedEventFeedback.getStudent().getId());
                savedEventFeedbackDTO.setEvent_id(savedEventFeedback.getEvent().getEvent_id());
                //savedEventFeedbackDTO.setEReg_id(savedEventFeedback.getE_reg_id());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Feedback Saved Successfully");
                responseDTO.setContent(savedEventFeedbackDTO);


//                EventOc savedEventOc =  eventOcRepo.save(modelMapper.map(eventOcDTO, EventOc.class));
//                EventOcDTO savedEventOcDTO = modelMapper.map(savedEventOc, EventOcDTO.class);
//                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Event Oc Saved Successfully");
//                responseDTO.setContent(savedEventOcDTO);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }


    }

    public ResponseDTO getAllEventFeedbacks(){

        try{
            List<EventFeedback> eventFeedbacksList = eventFeedbackRepo.findAll();
            if(!eventFeedbacksList.isEmpty()){
                List<EventFeedbackDTO> eventFeedbackDTOList = modelMapper.map(eventFeedbacksList, new TypeToken<List<EventFeedbackDTO>>(){}.getType());

                // Manually set the user id and Event_reg_id for each EventRegistrationDTO
                for (int i = 0; i < eventFeedbacksList.size(); i++) {
                    eventFeedbackDTOList.get(i).setUser_id(eventFeedbacksList.get(i).getStudent().getId());
                    //eventRegistrationDTOList.get(i).setEReg_id(eventFeedbacksList.get(i).getE_reg_id());
                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All event feedbacks Successfully");
                responseDTO.setContent(eventFeedbackDTOList);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event feedback Found");
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

    public ResponseDTO getEventFeedback(int eventFeedbackId){

        try{
            if(eventFeedbackRepo.existsById(eventFeedbackId)){
                EventFeedback eventFeedback = eventFeedbackRepo.findById(eventFeedbackId).orElse(null);
                EventFeedbackDTO eventFeedbackDTO =  modelMapper.map(eventFeedback, EventFeedbackDTO.class);

                // Manually set user_id
                eventFeedbackDTO.setUser_id(eventFeedback.getStudent().getId());
                //eventRegistrationDTO.setEReg_id(eventFeedback.getE_reg_id());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Feedback Retrieved Successfully");
                responseDTO.setContent(eventFeedbackDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Feedback found");
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


    public ResponseDTO updateEventFeedback(int eventFeedbackId, EventFeedbackDTO eventFeedbackDTO){
        try{
            if(eventFeedbackRepo.existsById(eventFeedbackId)){
                EventFeedback updatedEventFeedback =  eventFeedbackRepo.save(modelMapper.map(eventFeedbackDTO, EventFeedback.class));
                EventFeedbackDTO updatedEventFeedbackDTO = modelMapper.map(updatedEventFeedback, EventFeedbackDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Feedback Updated Successfully");
                responseDTO.setContent(updatedEventFeedbackDTO);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Feedback Found");
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


    public ResponseDTO deleteEventFeedback(int eventFeedbackId){
        try{
            if(eventFeedbackRepo.existsById(eventFeedbackId)){
                eventFeedbackRepo.deleteById(eventFeedbackId);
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Feedback deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Feedback Found");
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
