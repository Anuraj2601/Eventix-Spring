package com.example.eventix.service;

import com.example.eventix.dto.AnnouncementDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Announcements;
import com.example.eventix.repository.AnnouncementRepo;
import com.example.eventix.util.VarList;
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
    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO saveAnnouncement(AnnouncementDTO announcementDTO){
        try{
            if(announcementRepo.existsById(announcementDTO.getAnnouncement_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Announcement already exists");
                responseDTO.setContent(announcementDTO);

            }else{
                Announcements savedAnnouncement =  announcementRepo.save(modelMapper.map(announcementDTO, Announcements.class));
                AnnouncementDTO savedAnnouncementDTO = modelMapper.map(savedAnnouncement, AnnouncementDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Announcement Saved Successfully");
                responseDTO.setContent(savedAnnouncementDTO);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }


    }

    public ResponseDTO getAllAnnouncements(){

        try{
            List<Announcements> announcementsList = announcementRepo.findAll();
            if(!announcementsList.isEmpty()){
                List<AnnouncementDTO> announcementDTOList = modelMapper.map(announcementsList, new TypeToken<List<AnnouncementDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All Announcements Successfully");
                responseDTO.setContent(announcementDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Announcements Found");
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

    public ResponseDTO getAnnouncement(int announcementId){

        try{
            if(announcementRepo.existsById(announcementId)){
                Announcements announcement = announcementRepo.findById(announcementId).orElse(null);
                AnnouncementDTO announcementDTO =  modelMapper.map(announcement, AnnouncementDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Announcement Retrieved Successfully");
                responseDTO.setContent(announcementDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Announcement found");
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

    public ResponseDTO updateAnnouncement(AnnouncementDTO announcementDTO){
        try{
            if(announcementRepo.existsById(announcementDTO.getAnnouncement_id())){
                Announcements updatedAnnouncement =  announcementRepo.save(modelMapper.map(announcementDTO, Announcements.class));
                AnnouncementDTO updatedAnnouncementDTO = modelMapper.map(updatedAnnouncement, AnnouncementDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Announcement Updated Successfully");
                responseDTO.setContent(updatedAnnouncementDTO);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Announcement Found");
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

    public ResponseDTO deleteAnnouncement(AnnouncementDTO announcementDTO){
        try{
            if(announcementRepo.existsById(announcementDTO.getAnnouncement_id())){
                announcementRepo.deleteById(announcementDTO.getAnnouncement_id());
                //announcementRepo.delete(modelMapper.map(announcementDTO, Announcements.class));
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Announcement deleted Successfully");
                responseDTO.setContent(null);
            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Announcement Found");
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
}
