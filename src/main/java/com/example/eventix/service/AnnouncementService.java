package com.example.eventix.service;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.entity.Announcements;
import com.example.eventix.repository.AnnouncementRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<AnnouncementDTO> getAllAnnouncements(){
        List<Announcements> announcementsList = announcementRepo.findAll();
        return modelMapper.map(announcementsList, new TypeToken<List<AnnouncementDTO>>(){}.getType());
    }

    public AnnouncementDTO getAnnouncement(int announcementId){

        if(announcementRepo.existsById(announcementId)){
            Announcements announcement = announcementRepo.findById(announcementId).orElse(null);
            return modelMapper.map(announcement, AnnouncementDTO.class);
        }else{
            return null;
        }
    }

    public AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO){
        Announcements savedAnnouncement =  announcementRepo.save(modelMapper.map(announcementDTO, Announcements.class));
        return modelMapper.map(savedAnnouncement, AnnouncementDTO.class);

    }

    public boolean deleteAnnouncement(AnnouncementDTO announcementDTO){
        announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
        return true;
    }
}
