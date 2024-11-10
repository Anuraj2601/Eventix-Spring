package com.example.eventix.service;

import com.example.eventix.dto.EventOcDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.EventOc;
import com.example.eventix.repository.EventOcRepo;
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
public class EventOcService {

    @Autowired
    private EventOcRepo eventOcRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveEventOc(EventOcDTO eventOcDTO){
        try{
            if(eventOcRepo.existsById(eventOcDTO.getOc_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event Oc already exists");
                responseDTO.setContent(eventOcDTO);

            }else{

                EventOc eventOc = modelMapper.map(eventOcDTO, EventOc.class);

                eventOc.setMember(usersRepo.findById(eventOcDTO.getUser_id()).orElse(null));
                eventOc.setEvent(eventRepo.findById(eventOcDTO.getEvent_id()).orElse(null));


                EventOc savedEventOc = eventOcRepo.save(eventOc);
                EventOcDTO savedEventOcDTO = modelMapper.map(savedEventOc, EventOcDTO.class);

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Oc Saved Successfully");
                responseDTO.setContent(savedEventOcDTO);


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

    public ResponseDTO getAllEventOcs(){

        try{
            List<EventOc> eventOcsList = eventOcRepo.findAll();
            if(!eventOcsList.isEmpty()){
                List<EventOcDTO> eventOcDTOList = modelMapper.map(eventOcsList, new TypeToken<List<EventOcDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All event Ocs Successfully");
                responseDTO.setContent(eventOcDTOList);


            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Oc Found");
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

    public ResponseDTO getEventOc(int eventOcId){

        try{
            if(eventOcRepo.existsById(eventOcId)){
                EventOc eventOc = eventOcRepo.findById(eventOcId).orElse(null);
                EventOcDTO eventOcDTO =  modelMapper.map(eventOc, EventOcDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Oc Retrieved Successfully");
                responseDTO.setContent(eventOcDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Oc found");
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

    public ResponseDTO updateEventOc(int eventOc_id, EventOcDTO eventOcDTO){
        try{
            if(eventOcRepo.existsById(eventOc_id)){
                EventOc updatedEventOc =  eventOcRepo.save(modelMapper.map(eventOcDTO, EventOc.class));
                EventOcDTO updatedEventOcDTO = modelMapper.map(updatedEventOc, EventOcDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Oc Updated Successfully");
                responseDTO.setContent(updatedEventOcDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Oc Found");
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


    public ResponseDTO deleteEventOc(int eventOc_id){
        try{
            if(eventOcRepo.existsById(eventOc_id)){
                eventOcRepo.deleteById(eventOc_id);
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Oc deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event Oc Found");
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
