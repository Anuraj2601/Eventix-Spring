package com.example.eventix.service;

import com.example.eventix.dto.EventAnnouncementDTO;
import com.example.eventix.dto.NotificationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Notification;
import com.example.eventix.repository.NotificationRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;


    public ResponseDTO saveNotification(NotificationDTO notificationDTO){

        try{
            if(notificationRepo.existsById(notificationDTO.getNotification_id())){
                responseDTO.setStatusCode(VarList.RSP_DUPLICATED);
                responseDTO.setMessage("Notification already exists");
                responseDTO.setContent(notificationDTO);

            }else{
                Notification savedNotification =  notificationRepo.save(modelMapper.map(notificationDTO, Notification.class));
                NotificationDTO savedNotificationDTO = modelMapper.map(savedNotification, NotificationDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Notification Saved Successfully");
                responseDTO.setContent(savedNotificationDTO);

            }

            return responseDTO;

        }catch(Exception e){
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
            return responseDTO;
        }


    }

    public ResponseDTO getAllNotifications(){

        try{
            List<Notification> notificationsList = notificationRepo.findAll();
            if(!notificationsList.isEmpty()){
                List<NotificationDTO> notificationDTOList = modelMapper.map(notificationsList, new TypeToken<List<NotificationDTO>>(){}.getType());
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Retrieved All notifications Successfully");
                responseDTO.setContent(notificationDTOList);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No notifications Found");
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

    public ResponseDTO getNotification(int notificationId){

        try{
            if(notificationRepo.existsById(notificationId)){
                Notification notification = notificationRepo.findById(notificationId).orElse(null);
                NotificationDTO notificationDTO =  modelMapper.map(notification, NotificationDTO.class);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Notification Retrieved Successfully");
                responseDTO.setContent(notificationDTO);

            }else{
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Notification found");
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




}
