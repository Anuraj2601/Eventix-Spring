package com.example.eventix.service;

import com.example.eventix.dto.EventOcDTO;
import com.example.eventix.dto.EventRegistrationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventRegistration;
import com.example.eventix.repository.EventRegistrationRepo;
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
public class EventRegistrationService {

    @Autowired
    private EventRegistrationRepo eventRegistrationRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveEventRegistration(EventRegistrationDTO eventRegistrationDTO){
        try{
            if(eventRegistrationRepo.existsById(eventRegistrationDTO.getEvent_reg_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Registration already exists");
                responseDTO.setContent(eventRegistrationDTO);

            }else{

                EventRegistration eventRegistration = modelMapper.map(eventRegistrationDTO, EventRegistration.class);

                eventRegistration.setStudent(usersRepo.findById(eventRegistrationDTO.getUser_id()).orElse(null));
                eventRegistration.setEvent(eventRepo.findById(eventRegistrationDTO.getEvent_id()).orElse(null));


                EventRegistration savedEventRegistration = eventRegistrationRepo.save(eventRegistration);
                EventRegistrationDTO savedEventRegistrationDTO = modelMapper.map(savedEventRegistration, EventRegistrationDTO.class);

                // Manually set user_id
                savedEventRegistrationDTO.setUser_id(savedEventRegistration.getStudent().getId());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Registration Saved Successfully");
                responseDTO.setContent(savedEventRegistrationDTO);


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

    public ResponseDTO getAllEventRegistrations(){

        try{
            List<EventRegistration> eventRegistrationsList = eventRegistrationRepo.findAll();
            if(!eventRegistrationsList.isEmpty()){
                List<EventRegistrationDTO> eventRegistrationDTOList = modelMapper.map(eventRegistrationsList, new TypeToken<List<EventRegistrationDTO>>(){}.getType());

                // Manually set the published_user_id for each PostDTO
                for (int i = 0; i < eventRegistrationsList.size(); i++) {
                    eventRegistrationDTOList.get(i).setUser_id(eventRegistrationsList.get(i).getStudent().getId());
                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All event Registrations Successfully");
                responseDTO.setContent(eventRegistrationDTOList);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Registration Found");
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

    public ResponseDTO getEventRegistration(int eventRegId){

        try{
            if(eventRegistrationRepo.existsById(eventRegId)){
                EventRegistration eventRegistration = eventRegistrationRepo.findById(eventRegId).orElse(null);
                EventRegistrationDTO eventRegistrationDTO =  modelMapper.map(eventRegistration, EventRegistrationDTO.class);

                // Manually set user_id
                eventRegistrationDTO.setUser_id(eventRegistration.getStudent().getId());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Registration Retrieved Successfully");
                responseDTO.setContent(eventRegistrationDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Registration found");
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


    public ResponseDTO updateEventRegistration(int eventRegId, EventRegistrationDTO eventRegistrationDTO){
        try{
            if(eventRegistrationRepo.existsById(eventRegId)){
                EventRegistration updatedEventRegistration =  eventRegistrationRepo.save(modelMapper.map(eventRegistrationDTO, EventRegistration.class));
                //System.out.println("updated event OC" + updatedEventOc);
                EventRegistrationDTO updatedEventRegistrationDTO = modelMapper.map(updatedEventRegistration, EventRegistrationDTO.class);
                //System.out.println("updated event OC DTO" + updatedEventOcDTO);

                // Manually set user_id
                //updatedEventOcDTO.setUser_id(updatedEventOc.getMember().getId());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Registration Updated Successfully");
                responseDTO.setContent(updatedEventRegistrationDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Registration Found");
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


    public ResponseDTO deleteEventRegistration(int eventRegId){
        try{
            if(eventRegistrationRepo.existsById(eventRegId)){
                eventRegistrationRepo.deleteById(eventRegId);
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Registration deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Registration Found");
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
