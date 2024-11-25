package com.example.eventix.service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;


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
import java.util.Optional;

@Service
@Transactional
public class EventOcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

                // Manually set user_id
                savedEventOcDTO.setUser_id(savedEventOc.getMember().getId());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Oc Saved Successfully");
                responseDTO.setContent(savedEventOcDTO);
                try {
                    // Assuming oc_id is savedEventOc.getOc_id() and user_id is eventOcDTO.getUser_id()
                    jdbcTemplate.update("CALL update_event_oc_in_candidates('insert',  ?)",
                            savedEventOc.getOc_id());
                } catch (Exception e) {
                    // Log the error or handle failure of the stored procedure
                    System.err.println("Error calling stored procedure: " + e.getMessage());
                }


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


//    public ResponseDTO saveEventOc(EventOcDTO eventOcDTO) {
//        try {
//            Optional<EventOc> existingEventOc = eventOcRepo.findByMemberAndEvent(eventOcDTO.getUser_id(), eventOcDTO.getEvent_id());
//
//            if (existingEventOc.isPresent()) {
//                EventOc eventOc = existingEventOc.get();
//
//                if (eventOc.is_removed()) {
//                    // If is_removed is true, set it to false
//                    eventOc.set_removed(false);
//                    EventOc updatedEventOc = eventOcRepo.save(eventOc);
//
//                    EventOcDTO updatedEventOcDTO = modelMapper.map(updatedEventOc, EventOcDTO.class);
//                    updatedEventOcDTO.setUser_id(updatedEventOc.getMember().getId());
//
//                    responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                    responseDTO.setMessage("Event Oc reactivated successfully");
//                    responseDTO.setContent(updatedEventOcDTO);
//                } else {
//                    // If is_removed is already false, return a duplicate response
//                    responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
//                    responseDTO.setMessage("Event Oc already exists");
//                    responseDTO.setContent(eventOcDTO);
//                }
//
//            } else {
//                // If no existing EventOc, create a new one
//                EventOc eventOc = modelMapper.map(eventOcDTO, EventOc.class);
//
//                eventOc.setMember(usersRepo.findById(eventOcDTO.getUser_id()).orElse(null));
//                eventOc.setEvent(eventRepo.findById(eventOcDTO.getEvent_id()).orElse(null));
//                eventOc.set_removed(false); // Ensure is_removed is set to false
//
//                EventOc savedEventOc = eventOcRepo.save(eventOc);
//                EventOcDTO savedEventOcDTO = modelMapper.map(savedEventOc, EventOcDTO.class);
//
//                savedEventOcDTO.setUser_id(savedEventOc.getMember().getId());
//
//                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Event Oc saved successfully");
//                responseDTO.setContent(savedEventOcDTO);
//            }
//
//            return responseDTO;
//
//        } catch (Exception e) {
//            responseDTO.setStatusCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(e.getMessage());
//            responseDTO.setContent(null);
//            return responseDTO;
//        }
//    }


    public ResponseDTO getAllEventOcs(){

        try{
            List<EventOc> eventOcsList = eventOcRepo.findAll();
            if(!eventOcsList.isEmpty()){
                List<EventOcDTO> eventOcDTOList = modelMapper.map(eventOcsList, new TypeToken<List<EventOcDTO>>(){}.getType());

                // Manually set the published_user_id for each PostDTO
                for (int i = 0; i < eventOcsList.size(); i++) {
                    eventOcDTOList.get(i).setUser_id(eventOcsList.get(i).getMember().getId());
                }

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

                // Manually set user_id
                eventOcDTO.setUser_id(eventOc.getMember().getId());
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
                //System.out.println("updated event OC" + updatedEventOc);
                EventOcDTO updatedEventOcDTO = modelMapper.map(updatedEventOc, EventOcDTO.class);
                //System.out.println("updated event OC DTO" + updatedEventOcDTO);

                // Manually set user_id
                //updatedEventOcDTO.setUser_id(updatedEventOc.getMember().getId());

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

    public ResponseDTO removeEventOc(int eventOc_id) {
        try {
            if (eventOcRepo.existsById(eventOc_id)) {
                EventOc existingEventOc = eventOcRepo.findById(eventOc_id).orElseThrow(() -> new RuntimeException("Event OC not found"));

                // Call the stored procedure to update the candidates first
                try {
                    jdbcTemplate.update("CALL update_event_oc_in_candidates('delete', ?)", eventOc_id);
                } catch (DataAccessException e) {
                    // Log SQL-related exceptions handled by Spring JDBC
                    System.err.println("Error executing SQL operation: " + e.getMessage());
                    e.printStackTrace();

                    // Return error response if the stored procedure fails
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Error updating event OC in candidates");
                    responseDTO.setContent(e.getMessage());
                    return responseDTO;
                } catch (Exception e) {
                    // Log any general exceptions
                    System.err.println("Error calling stored procedure: " + e.getMessage());
                    e.printStackTrace();

                    // Return error response if the stored procedure fails
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Unexpected error occurred while updating event OC in candidates");
                    responseDTO.setContent(e.getMessage());
                    return responseDTO;
                }

                // After the stored procedure executes successfully, mark the Event OC as removed
                existingEventOc.set_removed(true);
                eventOcRepo.save(existingEventOc); // Save the updated event OC

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event OC status updated successfully");
                responseDTO.setContent(existingEventOc);

            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Event OC Found");
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


}
