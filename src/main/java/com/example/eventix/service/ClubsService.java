package com.example.eventix.service;

import com.example.eventix.dto.ClubsDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Clubs;
import com.example.eventix.entity.Post;
import com.example.eventix.entity.Users;
import com.example.eventix.repository.ClubsRepo;
import com.example.eventix.repository.UsersRepo;
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
public class ClubsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClubsRepo clubsRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private ResponseDTO responseDTO;


    private String buildImageUrl(String imageName) {
        return Optional.ofNullable(imageName)
                .map(name -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/static/clubs/")
                        .path(name)
                        .toUriString())
                .orElse(null);
    }

    public ResponseDTO getAllClubs() {
        try {
            List<Clubs> clubsList = clubsRepo.findAll();
            if (!clubsList.isEmpty()) {
                List<ClubsDTO> clubsDTOList = clubsList.stream().map(club -> {
                    ClubsDTO clubsDTO = modelMapper.map(club, ClubsDTO.class);
                    clubsDTO.setClub_image(buildImageUrl(club.getClub_image()));
                    return clubsDTO;
                }).toList();

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success retrieved All clubs");
                responseDTO.setContent(clubsList);
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

    public ResponseDTO getClub(Integer clubId) {
        try {
            // Check if the club exists
            if (!clubsRepo.existsById(clubId)) {
                // If not, return a 'No data found' response
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No data found");
                responseDTO.setContent(null);
                return responseDTO;
            }

            // Get the club details
            Clubs club = clubsRepo.findById(clubId).orElse(null);

            // Handle null case if club is not found, even after checking existence
            if (club == null) {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Club not found");
                responseDTO.setContent(null);
                return responseDTO;
            }

            // Map club entity to DTO
            ClubsDTO clubsDTO = modelMapper.map(club, ClubsDTO.class);

            // Handle image URL (ensure club image is not null)
            String clubImage = club.getClub_image();
            clubsDTO.setClub_image(buildImageUrl(club.getClub_image()));
            //clubsDTO.setClub_image(buildImageUrl(clubImage != null ? clubImage : "default-image.jpg"));

            // Set success response with the club DTO content
            responseDTO.setStatusCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfully retrieved Club");
            responseDTO.setContent(clubsDTO);

        } catch (Exception e) {
            // Handle any unexpected exceptions
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage("An error occurred: " + e.getMessage());
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

    public ResponseDTO saveClub(ClubsDTO clubsDTO, MultipartFile file) {
        try {
            if(clubsRepo.existsById(clubsDTO.getClub_id())) {
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Club already exists");
                responseDTO.setContent(clubsDTO);
            } else {
                clubsDTO.setClub_image(photoFunction.apply(0,file));

                //Manually setting up the club president id
//                Clubs club = modelMapper.map(clubsDTO, Clubs.class);
//                Users user = usersRepo.findById(clubsDTO.getClub_president_id())
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//                club.setUsers(user);
//
//                Clubs savedClub = clubsRepo.save(club);
//                ClubsDTO savedClubDTO = modelMapper.map(savedClub, ClubsDTO.class);
//                savedClubDTO.setClub_president_id(user.getId());
//                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
//                responseDTO.setMessage("Success Saved Club");
//                responseDTO.setContent(savedClubDTO);

                Clubs savedClub = clubsRepo.save(modelMapper.map(clubsDTO, Clubs.class));
                ClubsDTO savedClubDTO = modelMapper.map(savedClub, ClubsDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success Saved Club");
                responseDTO.setContent(savedClubDTO);
            }
            return responseDTO;

        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(clubsDTO);
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
            Path fileStorageLocation = Paths.get("src/main/resources/static/clubs").toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(randomFileName), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/clubs/" + randomFileName)
                    .toUriString();        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

    public ResponseDTO updateClub(Integer clubId,ClubsDTO clubsDTO,MultipartFile file) {
        try {
            if(clubsRepo.existsById(clubId)) {
                Clubs existingClub = clubsRepo.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));
                existingClub.setClub_name(clubsDTO.getClub_name());
                existingClub.setClub_image(photoFunction.apply(0,file));
                existingClub.setClub_description(clubsDTO.getClub_description());
                existingClub.setClub_in_charge(clubsDTO.getClub_in_charge());
                existingClub.set_deleted(clubsDTO.is_deleted());
                existingClub.setState(clubsDTO.isState());
                existingClub.setCreated_at(clubsDTO.getCreated_at());
                Clubs savedClub = clubsRepo.save(existingClub);
                ClubsDTO updatedClubDTO = modelMapper.map(savedClub, ClubsDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success updated Club");
                responseDTO.setContent(updatedClubDTO);
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

    public ResponseDTO deleteClub(Integer clubId) {
        try {
            if ( clubsRepo.existsById(clubId)) {
                clubsRepo.deleteById(clubId);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success deleted Club");
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

    public ResponseDTO updateClubState(Integer clubId, boolean newState) {
        try {
            if (clubsRepo.existsById(clubId)) {
                Clubs club = clubsRepo.findById(clubId)
                        .orElseThrow(() -> new RuntimeException("Club not found"));
                club.setState(newState);
                clubsRepo.save(club);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully updated club state");
                responseDTO.setContent(null);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Club not found");
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


    public ResponseDTO getClubById(Integer clubId) {
        try {
            if (clubsRepo.existsById(clubId)) {
                Clubs club = clubsRepo.findById(clubId).orElse(null);
                ClubsDTO clubsDTO = modelMapper.map(club, ClubsDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully retrieved club details");
                responseDTO.setContent(clubsDTO);
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Club not found");
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


}


