package com.example.eventix.service;

import com.example.eventix.dto.Event_SponsorDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Event_Sponsor;
import com.example.eventix.repository.Event_SponsorRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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

import static com.example.eventix.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
public class Event_SponsorService {
    @Autowired
    private Event_SponsorRepo event_sponsorRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveSponsor(Event_SponsorDTO event_sponsorDTO, MultipartFile file) {
        try {
            if(event_sponsorRepo.existsById(event_sponsorDTO.getSponsor_id())) {
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Sponsor already exists");
                responseDTO.setContent(event_sponsorDTO);
            } else {
                event_sponsorDTO.setCompany_logo(photoFunction.apply(0,file));
                Event_Sponsor savedSponsor = event_sponsorRepo.save(modelMapper.map(event_sponsorDTO, Event_Sponsor.class));
                Event_SponsorDTO savedSponsorDTO = modelMapper.map(savedSponsor, Event_SponsorDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setContent(savedSponsorDTO);
            }
            return responseDTO;

        } catch(Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(event_sponsorDTO);
            return responseDTO;
        }
    }

    public ResponseDTO getAllSponsors() {
        try {
            List<Event_Sponsor> event_sponsorsList = event_sponsorRepo.findAll();
            if(!event_sponsorsList.isEmpty()) {
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully retrieved all Sponsors");
                responseDTO.setContent(event_sponsorsList);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No data found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        } catch(Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    public ResponseDTO getSponsor(int sponsorId) {
        try {
            if(event_sponsorRepo.existsById(sponsorId)) {
                Event_Sponsor event_sponsor = event_sponsorRepo.findById(sponsorId).orElse(null);
                Event_SponsorDTO event_sponsorDTO = modelMapper.map(event_sponsor, Event_SponsorDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(event_sponsorDTO);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No data found");
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

    public ResponseDTO updateSponsor(Integer sponsorId,Event_SponsorDTO event_sponsorDTO, MultipartFile file) {
        try {
            if(event_sponsorRepo.existsById(sponsorId)) {
                Event_Sponsor existingSponsor = event_sponsorRepo.findById(sponsorId).orElseThrow(() -> new RuntimeException("Sponsor not found"));

                existingSponsor.setSponsor_description(event_sponsorDTO.getSponsor_description());
                existingSponsor.setSponsor_name(event_sponsorDTO.getSponsor_name());
                existingSponsor.setAmount(event_sponsorDTO.getAmount());
                existingSponsor.setSponsorType(event_sponsorDTO.getSponsorType());
                existingSponsor.setContact_email(event_sponsorDTO.getContact_email());
                existingSponsor.setContact_person(event_sponsorDTO.getContact_person());
                existingSponsor.setCompany_logo(photoFunction.apply(sponsorId,file));
               Event_Sponsor savedSponsor = event_sponsorRepo.save(existingSponsor);
                //Event_Sponsor savedSponsor = event_sponsorRepo.save(modelMapper.map(event_sponsorDTO, Event_Sponsor.class));
                Event_SponsorDTO updatedSponsorDTO = modelMapper.map(savedSponsor, Event_SponsorDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(updatedSponsorDTO);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No data found");
                responseDTO.setContent(null);
            }
            return responseDTO;

        } catch(Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    public ResponseDTO deleteSponsor(Integer sponsorId) {
        try {
            if(event_sponsorRepo.existsById(sponsorId)) {
                event_sponsorRepo.deleteById(sponsorId);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Sponsor deleted Successfully");
                responseDTO.setContent(null);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No data found");
                responseDTO.setContent(null);
            }

            return responseDTO;

        } catch(Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

//    private final BiFunction<Integer,MultipartFile,String> photoFunction = (id,image) -> {
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
//            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/static/image/" + randomFileName).toUriString();
//        } catch (Exception exception) {
//            throw new RuntimeException("Unable to save image");
//        }
//    };
private final BiFunction<Integer, MultipartFile, String> photoFunction = (id, image) -> {
    try {
        // Define the directory for storing sponsor logos
        String uploadDirectory = "src/main/resources/static/uploads/sponsor-logos/";
        String originalFilename = image.getOriginalFilename();
        String fileExtension1 = fileExtension.apply(originalFilename);
        String randomFileName = UUID.randomUUID().toString() + fileExtension1;

        // Construct the path to save the file
        Path filePath = Paths.get(uploadDirectory, randomFileName);

        // Ensure the directory exists
        Files.createDirectories(filePath.getParent());

        // Save the file to the defined path
        Files.write(filePath, image.getBytes());

        // Construct the URL to access the image
        String baseUrl = "http://localhost:8080/uploads/sponsor-logos/";
        return baseUrl + randomFileName;

    } catch (Exception exception) {
        throw new RuntimeException("Unable to save sponsor logo", exception);
    }
};


}
