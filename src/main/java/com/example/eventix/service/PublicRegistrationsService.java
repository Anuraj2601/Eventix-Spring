package com.example.eventix.service;

import com.example.eventix.dto.PublicRegistrationsDTO;
import com.example.eventix.entity.PublicRegistrations;
import com.example.eventix.repository.PublicRegistrationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicRegistrationsService {

    @Autowired
    private PublicRegistrationsRepository publicRegistrationsRepository;

    // Save new public registration
    // In the service, make sure you're setting all properties properly
    public PublicRegistrations savePublicRegistration(PublicRegistrationsDTO registrationDTO) {
        PublicRegistrations registration = new PublicRegistrations();
        registration.setClubId(registrationDTO.getClubId());
        registration.setEventId(registrationDTO.getEventId());
        registration.setEventName(registrationDTO.getEventName());
        registration.setParticipantName(registrationDTO.getParticipantName());
        registration.setEmail(registrationDTO.getEmail());
        registration.setMobile(registrationDTO.getMobile());
        registration.setRegistrationTime(registrationDTO.getRegistrationTime());
        registration.setCheckInStatus(0); // Default value

        // Log the values to check if they are being set correctly
        System.out.println("Saving registration: " + registration);

        return publicRegistrationsRepository.save(registration);
    }


    // Get all public registrations
    public List<PublicRegistrationsDTO> getAllPublicRegistrations() {
        List<PublicRegistrations> registrations = publicRegistrationsRepository.findAll();

        // Convert the list of entities to a list of DTOs
        return registrations.stream().map(registration -> new PublicRegistrationsDTO(
                registration.getCheckInStatus(),

                registration.getId(),
                registration.getEventId(),
                registration.getClubId(),
                registration.getEventName(),
                registration.getParticipantName(),
                registration.getEmail(),
                registration.getMobile(),
                registration.getRegistrationTime()
        )).collect(Collectors.toList());
    }

    // Get public registrations by event ID
    public List<PublicRegistrationsDTO> getPublicRegistrationsByEvent(Long eventId) {
        List<PublicRegistrations> registrations = publicRegistrationsRepository.findByEventId(eventId);

        // Convert the list of entities to a list of DTOs
        return registrations.stream().map(registration -> new PublicRegistrationsDTO(
                registration.getCheckInStatus(),
                registration.getId(),
                registration.getEventId(),
                registration.getClubId(),
                registration.getEventName(),
                registration.getParticipantName(),
                registration.getEmail(),
                registration.getMobile(),
                registration.getRegistrationTime()

        )).collect(Collectors.toList());
    }

    // Get public registration by ID
    public PublicRegistrationsDTO getPublicRegistrationById(Long id) {
        Optional<PublicRegistrations> registration = publicRegistrationsRepository.findById(id);
        if (registration.isPresent()) {
            PublicRegistrations reg = registration.get();
            return new PublicRegistrationsDTO(
                    reg.getCheckInStatus(),
                    reg.getId(),
                    reg.getEventId(),
                    reg.getClubId(),
                    reg.getEventName(),
                    reg.getParticipantName(),
                    reg.getEmail(),
                    reg.getMobile(),
                    reg.getRegistrationTime()

            );
        } else {
            throw new RuntimeException("Public registration not found");
        }
    }

    // Update check-in status of a registration (to mark as checked-in)
    public Object updateCheckInStatus(Long id, int checkInStatus) {
        // Find the registration by ID
        Optional<PublicRegistrations> registrationOpt = publicRegistrationsRepository.findById(id);

        // Check if the registration exists
        if (registrationOpt.isPresent()) {
            PublicRegistrations registration = registrationOpt.get();

            // Update the check-in status
            registration.setCheckInStatus(checkInStatus);

            // Save the updated registration object
            publicRegistrationsRepository.save(registration);

            // Return success message and updated registration data
            return new Object() {
                public String message = "Check-in status updated successfully.";
                public Object data = registration;
            };
        } else {
            // Throw an exception if the registration is not found
            throw new RuntimeException("Public registration not found");
        }
    }

}
