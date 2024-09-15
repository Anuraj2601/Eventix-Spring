package com.example.eventix.service;

import com.example.eventix.entity.Registration;
import com.example.eventix.repository.RegistrationRepo;
import com.example.eventix.dto.RegistrationDTO;
import com.example.eventix.entity.Clubs;
import com.example.eventix.repository.ClubsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepo registrationRepository;

    @Autowired
    private ClubsRepo clubsRepository;

    // Create or update a registration
    public RegistrationDTO saveRegistration(RegistrationDTO registrationDTO) {
        Registration registration = new Registration();
        registration.setRegistration_id(registrationDTO.getRegistration_id()); // Set ID for update
        registration.setFullName(registrationDTO.getFullName());
        registration.setEmail(registrationDTO.getEmail());
        registration.setRegisterNo(registrationDTO.getRegisterNo());
        registration.setIndexNo(registrationDTO.getIndexNo());
        registration.setTeam(registrationDTO.getTeam());
        registration.setYearOfStudy(registrationDTO.getYearOfStudy());
        registration.setInterviewSlot(registrationDTO.getInterviewSlot());
        registration.setReason(registrationDTO.getReason());

        Optional<Clubs> club = clubsRepository.findById(registrationDTO.getClub_id()); // Use club_id here
        if (club.isPresent()) {
            registration.setClub(club.get());
        } else {
            throw new RuntimeException("Club not found");
        }

        Registration savedRegistration = registrationRepository.save(registration);
        registrationDTO.setRegistration_id(savedRegistration.getRegistration_id());
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
        dto.setRegistration_id(registration.getRegistration_id());
        dto.setFullName(registration.getFullName());
        dto.setEmail(registration.getEmail());
        dto.setRegisterNo(registration.getRegisterNo());
        dto.setIndexNo(registration.getIndexNo());
        dto.setTeam(registration.getTeam());
        dto.setYearOfStudy(registration.getYearOfStudy());
        dto.setInterviewSlot(registration.getInterviewSlot());
        dto.setReason(registration.getReason());
        dto.setClub_id(registration.getClub().getClub_id()); // Use club_id here
        return dto;
    }
}
