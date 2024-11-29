package com.example.eventix.service;

import com.example.eventix.dto.NotificationDTO;
import com.example.eventix.entity.Registration;
import com.example.eventix.entity.Users;
import com.example.eventix.entity.Clubs;
import com.example.eventix.repository.RegistrationRepo;
import com.example.eventix.dto.RegistrationDTO;
import com.example.eventix.repository.ClubsRepo;
import com.example.eventix.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepo registrationRepository;

    @Autowired
    private ClubsRepo clubsRepository;

    @Autowired
    private UsersRepo usersRepository;

    @Autowired
    private NotificationService notificationService;

    // Create or update a registration
//    public RegistrationDTO saveRegistration(RegistrationDTO registrationDTO) {
//        Registration registration = new Registration();
//        registration.setRegistrationId(registrationDTO.getRegistrationId()); // Set ID for update
//        registration.setTeam(registrationDTO.getTeam());
//        registration.setInterviewSlot(registrationDTO.getInterviewSlot());
//        registration.setReason(registrationDTO.getReason());
//
//        // Only set 'accepted' if provided, else default will be used
//        registration.setAccepted(registrationDTO.getAccepted());
//        // Only set 'position' if provided, else default will be used
//        registration.setPosition(registrationDTO.getPosition());
//
//        Optional<Clubs> club = clubsRepository.findById(registrationDTO.getClubId());
//        if (club.isPresent()) {
//            registration.setClub(club.get());
//        } else {
//            throw new RuntimeException("Club not found");
//        }
//
//        Optional<Users> user = usersRepository.findByEmail(registrationDTO.getEmail());
//        if (user.isPresent()) {
//            registration.setUser(user.get());
//        } else {
//            throw new RuntimeException("User not found");
//        }
//
//        Registration savedRegistration = registrationRepository.save(registration);
//        registrationDTO.setRegistrationId(savedRegistration.getRegistrationId());
//        return registrationDTO;
//    }


    public RegistrationDTO saveRegistration(RegistrationDTO registrationDTO) {
        Registration registration = new Registration();
        registration.setRegistrationId(registrationDTO.getRegistrationId()); // Set ID for update
        registration.setTeam(registrationDTO.getTeam());
        registration.setInterviewSlot(registrationDTO.getInterviewSlot());
        registration.setReason(registrationDTO.getReason());

        // Only set 'accepted' if provided, else default will be used
        registration.setAccepted(registrationDTO.getAccepted());
        // Only set 'position' if provided, else default will be used
        registration.setPosition(registrationDTO.getPosition());

        Optional<Clubs> club = clubsRepository.findById(registrationDTO.getClubId());
        if (club.isPresent()) {
            registration.setClub(club.get());
        } else {
            throw new RuntimeException("Club not found");
        }

        Optional<Users> user = usersRepository.findByEmail(registrationDTO.getEmail());
        if (user.isPresent()) {
            System.out.println("user"+ user);
            System.out.println("user.get()"+ user.get());
            System.out.println("user.get().getID()" + user.get().getId());
            registration.setUser(user.get());
        } else {
            throw new RuntimeException("User not found");
        }

        Registration savedRegistration = registrationRepository.save(registration);
        registrationDTO.setRegistrationId(savedRegistration.getRegistrationId());

        // Check if 'accepted' is true and send notification
        if (registrationDTO.getAccepted() == 1) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotification("Your registration has been accepted for Club "+ club.get().getClub_name() );
            notificationDTO.set_read(false);
            notificationDTO.setUser_id(user.get().getId());
            notificationDTO.setClub_id(club.get().getClub_id());


            notificationService.saveNotification(notificationDTO); // Send notification
        }

        return registrationDTO;
    }

    // Delete registration
    public void deleteRegistration(int id) {
        if (registrationRepository.existsById(id)) {
            registrationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Registration not found");
        }
    }

    // Get all registrations
    public List<RegistrationDTO> getAllRegistrations() {
        return registrationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get registration by ID
    public RegistrationDTO getRegistration(int id) {
        Optional<Registration> registration = registrationRepository.findById(id);
        return registration.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    // Convert Registration entity to RegistrationDTO
    private RegistrationDTO convertToDTO(Registration registration) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setRegistrationId(registration.getRegistrationId());
        dto.setTeam(registration.getTeam());
        dto.setInterviewSlot(registration.getInterviewSlot());
        dto.setReason(registration.getReason());
        dto.setClubId(registration.getClub().getClub_id()); // Updated field name
        dto.setEmail(registration.getUser().getEmail()); // Use getEmail()
        dto.setAccepted(registration.getAccepted());
        dto.setPosition(registration.getPosition());

        dto.setCreatedAt(registration.getCreatedAt());

        dto.setUserId(registration.getUser().getId());

        return dto;
    }

}
