package com.example.eventix.service;

import com.example.eventix.dto.EventDTO;
import com.example.eventix.dto.MeetingDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Event;
import com.example.eventix.entity.Meeting;
import com.example.eventix.repository.EventRepo;
import com.example.eventix.util.VarList;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private EmailService emailService;


    public ResponseDTO saveEvent(EventDTO eventDTO, MultipartFile eventImage, MultipartFile budgetFile) {
        try {
            //Check if event already exists
            if (eventRepo.existsById(eventDTO.getEvent_id())) {
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Event already exists");
                responseDTO.setContent(eventDTO);
                return responseDTO;
            } else {
                // Ensure budget file is provided
                if (budgetFile == null || budgetFile.isEmpty()) {
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Budget file is required");
                    responseDTO.setContent(null);
                    return responseDTO;
                }

                // Handle the optional image file with exception handling
                try {
                    if (eventImage != null && !eventImage.isEmpty()) {
                        eventDTO.setEvent_image(photoFunction.apply(0, eventImage));
                    } else {
                        eventDTO.setEvent_image(null);
                    }
                } catch (Exception e) {
                    log.error("Error saving event image: {}", e.getMessage());
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Failed to save event image");
                    responseDTO.setContent(null);
                    return responseDTO;
                }

                // Handle the mandatory budget file with exception handling
                try {
                    eventDTO.setBudget_pdf(pdfFunction.apply(0, budgetFile));
                } catch (Exception e) {
                    log.error("Error saving budget file: {}", e.getMessage());
                    responseDTO.setStatusCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Failed to save budget file");
                    responseDTO.setContent(null);
                    return responseDTO;
                }

                // Set default value for iud_status to false
                eventDTO.setIud_status(-1);
                eventDTO.setBudget_status(-1);

                // Save the event
                Event savedEvent = eventRepo.save(modelMapper.map(eventDTO, Event.class));
                EventDTO savedEventDTO = modelMapper.map(savedEvent, EventDTO.class);

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event Saved Successfully");
                responseDTO.setContent(savedEventDTO);
                return responseDTO;
            }

        } catch (Exception e) {
            log.error("Error saving event: {}", e.getMessage());
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
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
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/events/eventImages/" + randomFileName).toUriString();
        } catch (Exception e) {
            log.error("Failed to save image: {}", e.getMessage());
            return null;
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
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/events/eventBudgets/" + randomFileName).toUriString();
        } catch (Exception e) {
            log.error("Failed to save PDF: {}", e.getMessage());
            return null;
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



    public ResponseDTO getAllEventsWithClubDetails() {
        try {
            List<Event> events = eventRepo.findAllEventsWithClubDetails(); // Fetch events with clubs
            if (!events.isEmpty()) {
                // Map events to DTOs including club and president details
                List<EventDTO> eventDTOList = events.stream().map(event -> {
                    EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
                    if (event.getClub() != null) {
                        eventDTO.setClubImage(event.getClub().getClub_image()); // Map club image
                        eventDTO.setClubInCharge(event.getClub().getClub_in_charge());

                        // Map club president's image
                        if (event.getClub().getPresident() != null) {
                            eventDTO.setClubPresidentImage(event.getClub().getPresident().getPhotoUrl());
                            eventDTO.setClubPresidentName(event.getClub().getPresident().getFirstname());
                        }
                    }
                    return eventDTO;
                }).collect(Collectors.toList());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Events with Club and President Details Successfully");
                responseDTO.setContent(eventDTOList);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Events Found");
                responseDTO.setContent(null);
            }
            return responseDTO;
        } catch (Exception e) {
            log.error("Error retrieving events with club and president details: {}", e.getMessage());
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


    public ResponseDTO updateBudgetStatus(int eventId, int status) {
        try {
            // Find the event by ID
            Optional<Event> eventOptional = eventRepo.findById(eventId);
            if (eventOptional.isPresent()) {
                Event event = eventOptional.get();
                event.setBudget_status(status); // Update the budget status

                // Save the updated event back to the repository
                eventRepo.save(event);

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Budget status updated successfully.");
                responseDTO.setContent(event); // Optionally return the updated event
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Event not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error updating budget status: " + e.getMessage());
            responseDTO.setContent(null);
        }
        return responseDTO;
    }


    public ResponseDTO updateIudStatus(int eventId, int status) {
        try {
            // Find the event by ID
            Optional<Event> eventOptional = eventRepo.findById(eventId);
            if (eventOptional.isPresent()) {
                Event event = eventOptional.get();
                event.setIud_status(status); // Update the budget status

                // Save the updated event back to the repository
                eventRepo.save(event);

                if (status == 1) { // Assuming 1 means approval
                    String recipientEmail = event.getClub().getClub_in_charge(); // Update with actual email field
                    String eventDetails = "Date: " + event.getDate() + ", Time: " + event.getTime() + ", Venue: " + event.getVenue();
                    String clubName = event.getClub().getClub_name();
                    emailService.sendEventApprovalEmail(recipientEmail, event.getName(), eventDetails, clubName);
                }

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("IUD status updated successfully.");
                responseDTO.setContent(event); // Optionally return the updated event
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Event not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error updating IUD status: " + e.getMessage());
            responseDTO.setContent(null);
        }
        return responseDTO;
    }


    public ResponseDTO updateEventById(int event_id, EventDTO eventDTO, MultipartFile eventImage, MultipartFile budgetFile) {
        try {
            log.info("Updating event with ID: {}", event_id);
            // Check if the event exists
            Optional<Event> existingEventOpt = eventRepo.findById(event_id);
            if (existingEventOpt.isPresent()) {
                Event existingEvent = existingEventOpt.get();

                // Update fields from EventDTO
                existingEvent.setName(eventDTO.getName());
                existingEvent.setVenue(eventDTO.getVenue());
                existingEvent.setDate(eventDTO.getDate());
                existingEvent.setTime(eventDTO.getTime());
                existingEvent.setPurpose(eventDTO.getPurpose());
                existingEvent.setBenefits(eventDTO.getBenefits());
                existingEvent.setPublic_status(eventDTO.isPublic_status());

                // Handle optional event image
                if (eventImage != null && !eventImage.isEmpty()) {
                    try {
                        existingEvent.setEvent_image(photoFunction.apply(event_id, eventImage));
                    } catch (Exception e) {
                        log.error("Error updating event image: {}", e.getMessage());
                        responseDTO.setStatusCode(VarList.RSP_ERROR);
                        responseDTO.setMessage("Failed to update event image");
                        responseDTO.setContent(null);
                        return responseDTO;
                    }
                }

                // Handle optional budget file
                if (budgetFile != null && !budgetFile.isEmpty()) {
                    try {
                        existingEvent.setBudget_pdf(pdfFunction.apply(event_id, budgetFile));
                    } catch (Exception e) {
                        log.error("Error updating budget file: {}", e.getMessage());
                        responseDTO.setStatusCode(VarList.RSP_ERROR);
                        responseDTO.setMessage("Failed to update budget file");
                        responseDTO.setContent(null);
                        return responseDTO;
                    }
                }

                // Save the updated event
                Event updatedEvent = eventRepo.save(existingEvent);
                EventDTO updatedEventDTO = modelMapper.map(updatedEvent, EventDTO.class);

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event updated successfully");
                responseDTO.setContent(updatedEventDTO);
                return responseDTO;
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Event not found");
                responseDTO.setContent(null);
                return responseDTO;
            }
        } catch (Exception e) {
            log.error("Error updating event: {}", e.getMessage());
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }




    public ResponseDTO deleteEventById(int event_id) {
        try {
            if (eventRepo.existsById(event_id)) {
                eventRepo.deleteById(event_id);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Event deleted successfully.");
                responseDTO.setContent(null);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Event not found.");
                responseDTO.setContent(null);
            }
        } catch (Exception e) {
            log.error("Error deleting event: {}", e.getMessage());
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error deleting event: " + e.getMessage());
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    public byte[] generateEventProposal(int eventId) {
        try {
            // Fetch event details from the database
            Optional<Event> eventOptional = eventRepo.findById(eventId);
            if (eventOptional.isEmpty()) {
                throw new RuntimeException("Event not found for ID: " + eventId);
            }

            Event event = eventOptional.get();

            // Prepare a ByteArrayOutputStream to hold the PDF content
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);


            // Add event details to the PDF
            document.add(new Paragraph("Event Proposal").setBold().setFontSize(18));
            document.add(new Paragraph("Event Name: " + event.getName()));
            document.add(new Paragraph("Date: " + event.getDate()));
            document.add(new Paragraph("Time: " + event.getTime()));
            document.add(new Paragraph("Venue: " + event.getVenue()));
            document.add(new Paragraph("Purpose: " + event.getPurpose()));
            document.add(new Paragraph("Benefits: " + event.getBenefits()));

            // Close the document
            document.close();

            // Return the generated PDF as a byte array
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating event proposal: " + e.getMessage(), e);
        }
    }


}
