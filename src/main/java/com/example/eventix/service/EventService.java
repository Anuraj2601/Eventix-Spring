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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.eventix.constant.Constant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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

//    public ResponseDTO saveEvent(EventDTO eventDTO, MultipartFile file) {
//        try{
//            if(eventRepo.existsById(eventDTO.getEvent_id())){
//                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
//                responseDTO.setMessage("Event already exists");
//                responseDTO.setContent(eventDTO);
//
//            }else{
//                eventDTO.setImageUrl(photoFunction.apply(0,file));
//                Event savedEvent = eventRepo.save(modelMapper.map(eventDTO, Event.class));
//                EventDTO savedEventDTO = modelMapper.map(savedEvent, EventDTO.class);
//                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Event Saved Successfully");
//                responseDTO.setContent(savedEventDTO);
//            }
//
//            return responseDTO;
//
//        }catch(Exception e){
//
//            responseDTO.setStatusCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(e.getMessage());
//            responseDTO.setContent(null);
//            return responseDTO;
//
//        }
//    }


    public ResponseDTO saveEvent(EventDTO eventDTO, MultipartFile eventImage, MultipartFile budgetFile) {
        try {
            //Check if event already exists
            if (eventRepo.existsById(eventDTO.getEvent_id())) {
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event already exists");
                responseDTO.setContent(eventDTO);
            } else {
                // Ensure budget file is provided
                if (budgetFile == null || budgetFile.isEmpty()) {
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Budget file is required");
                    responseDTO.setContent(null);
                    return responseDTO;
                }

                // Handle the optional image file
                if (eventImage != null && !eventImage.isEmpty()) {
                    eventDTO.setEvent_image(photoFunction.apply(0, eventImage));
                } else {
                    eventDTO.setEvent_image(null);
                }

                // Handle the mandatory budget file
                eventDTO.setBudget_pdf(pdfFunction.apply(0, budgetFile));

                // Set default value for iud_status to false
                eventDTO.setIud_status(false);
                eventDTO.setBudget_status(false);

                // Save the event
                Event savedEvent = eventRepo.save(modelMapper.map(eventDTO, Event.class));
                EventDTO savedEventDTO = modelMapper.map(savedEvent, EventDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Saved Successfully");
                responseDTO.setContent(savedEventDTO);
            }
            return responseDTO;

        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(eventDTO);
            return responseDTO;
        }
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<Integer,MultipartFile,String> photoFunction = (id, image) -> {
        String originalFilename = image.getOriginalFilename();
        String fileExtension1 = fileExtension.apply(originalFilename);

        String randomFileName = UUID.randomUUID().toString() + fileExtension1;
        try {
            Path fileStorageLocation = Paths.get(EVENT_PHOTO_DIRECTORY).toAbsolutePath().normalize();

            // Log the file storage path
            System.out.println("File Storage Location: " + fileStorageLocation.toString());

            // Check if directory exists, if not, create it
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
                System.out.println("Directory created: " + fileStorageLocation.toString());
            } else {
                System.out.println("Directory already exists: " + fileStorageLocation.toString());
            }

            // Copy the file to the directory
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(randomFileName), REPLACE_EXISTING);
            System.out.println("File saved as: " + fileStorageLocation.resolve(randomFileName).toString());

            // Return the URL of the saved file
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/static/image/" + randomFileName).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image", exception);
        }

    };


    private final BiFunction<Integer,MultipartFile,String> pdfFunction = (id, budget) -> {
        String originalFilename = budget.getOriginalFilename();
        String fileExtension1 = fileExtension.apply(originalFilename);

        // Ensure the file extension is PDF
        if (!".pdf".equalsIgnoreCase(fileExtension1)) {
            throw new RuntimeException("Invalid file type. Only PDF files are allowed.");
        }

        String randomFileName = UUID.randomUUID().toString() + fileExtension1;
        try {
            Path fileStorageLocation = Paths.get(EVENT_BUDGET_DIRECTORY).toAbsolutePath().normalize();

            // Log the file storage path
            System.out.println("File Storage Location: " + fileStorageLocation.toString());

            // Check if directory exists, if not, create it
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
                System.out.println("Directory created: " + fileStorageLocation.toString());
            } else {
                System.out.println("Directory already exists: " + fileStorageLocation.toString());
            }

            // Copy the file to the directory
            Files.copy(budget.getInputStream(), fileStorageLocation.resolve(randomFileName), REPLACE_EXISTING);
            System.out.println("File saved as: " + fileStorageLocation.resolve(randomFileName).toString());

            // Return the URL of the saved file
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/static/pdf/" + randomFileName).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save budget file", exception);
        }

    };


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


    public ResponseDTO getEventsByClubId(int clubId) {
        try {
            List<Event> eventsList = eventRepo.findEventsByClubId(clubId);
            if (!eventsList.isEmpty()) {
                List<EventDTO> eventDTOList = modelMapper.map(eventsList, new TypeToken<List<EventDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Events Retrieved Successfully");
                responseDTO.setContent(eventDTOList);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Events Found for this Club");
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

//    public ResponseDTO updateEvent(int event_id, EventDTO eventDTO, MultipartFile file){
//        try{
//            if(eventRepo.existsById(event_id)){
//
//                eventDTO.setImageUrl(photoFunction.apply(0,file));
//                Event updatedEvent =  eventRepo.save(modelMapper.map(eventDTO, Event.class));
//                EventDTO updatedEventDTO = modelMapper.map(updatedEvent, EventDTO.class);
//                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Event Updated Successfully");
//                responseDTO.setContent(updatedEventDTO);
//
//
//            }else{
//                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
//                responseDTO.setMessage("No Event Found");
//                responseDTO.setContent(null);
//
//            }
//
//            return responseDTO;
//
//        }catch(Exception e){
//            responseDTO.setStatusCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(e.getMessage());
//            responseDTO.setContent(null);
//            return responseDTO;
//        }
//    }

//    public ResponseDTO deleteEvent(int event_id){
//        try{
//            if(eventRepo.existsById(event_id)){
//                eventRepo.deleteById(event_id);
//                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Event Deleted Successfully");
//                responseDTO.setContent(null);
//
//
//            }else{
//                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
//                responseDTO.setMessage("No Event Found");
//                responseDTO.setContent(null);
//
//            }
//
//            return responseDTO;
//
//        }catch(Exception e){
//            responseDTO.setStatusCode(VarList.RSP_ERROR);
//            responseDTO.setMessage(e.getMessage());
//            responseDTO.setContent(e);
//            return responseDTO;
//        }
//    }


//    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
//            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
//
//    private final BiFunction<Integer,MultipartFile,String> photoFunction = (id, image) -> {
//        String originalFilename = image.getOriginalFilename();
//        String fileExtension1 = fileExtension.apply(originalFilename);
//
//        String randomFileName = UUID.randomUUID().toString() + fileExtension1;
//        try {
//            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
//            if (!Files.exists(fileStorageLocation)) {
//                Files.createDirectories(fileStorageLocation);
//            }
//            Files.copy(image.getInputStream(),fileStorageLocation.resolve(randomFileName), REPLACE_EXISTING);
//            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/static/image/events/" + randomFileName).toUriString();
//        } catch (Exception exception) {
//            throw new RuntimeException("Unable to save image");
//        }
//    };


}
