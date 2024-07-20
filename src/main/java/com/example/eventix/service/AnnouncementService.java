package com.example.eventix.service;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.entity.Announcements;
import com.example.eventix.repository.AnnouncementRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnnouncementService {

    @Autowired
    private AnnouncementRepo announcementRepo;
    @Autowired
    private ModelMapper modelMapper;

    public AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO){
        Announcements savedAnnouncement =  announcementRepo.save(modelMapper.map(announcementDTO, Announcements.class));
        return modelMapper.map(savedAnnouncement, AnnouncementDTO.class);


    }
}
