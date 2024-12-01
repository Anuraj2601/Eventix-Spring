package com.example.eventix.service;

import com.example.eventix.dto.NotificationDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.entity.Notification;
import com.example.eventix.repository.NotificationRepo;
import com.example.eventix.repository.UsersRepo;
import com.example.eventix.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UsersRepo usersRepo;

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

                Notification notification = modelMapper.map(notificationDTO, Notification.class);

                notification.setStudent(usersRepo.findById(notificationDTO.getUser_id()).orElse(null));


                Notification savedNotification =  notificationRepo.save(notification);
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

    public ResponseDTO getNotificationsByUserId(int userId) {
        try {
            // Fetch notifications from the repository
            List<Notification> notifications = notificationRepo.findByUserId(userId);

            if (notifications.isEmpty()) {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No notifications found for the user");
                responseDTO.setContent(null);
            } else {
                // Map notifications to DTOs
                List<NotificationDTO> notificationDTOs = notifications.stream().map(notification -> {
                    NotificationDTO dto = modelMapper.map(notification, NotificationDTO.class); // Map other fields
                    if (notification.getStudent() != null) { // Ensure student is not null
                        dto.setUser_id(notification.getStudent().getId()); // Manually set user_id
                    }
                    return dto;
                }).collect(Collectors.toList());

                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Notifications retrieved successfully");
                responseDTO.setContent(notificationDTOs);
            }
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }



    public ResponseDTO markAllAsRead(int user_id) {
        try {
            // Fetch all notifications for the given user_id
            List<Notification> notifications = notificationRepo.findByUserId(user_id);

            if (notifications.isEmpty()) {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No notifications found for the user");
                responseDTO.setContent(null);
                return responseDTO;
            }

            // Update is_read to true only if it is currently false
            boolean anyUpdated = false;
            for (Notification notification : notifications) {
                if (!notification.is_read()) { // Check if is_read is false
                    notification.set_read(true);
                    anyUpdated = true; // Track if any notifications were updated
                }
            }

            if (anyUpdated) {
                // Save only if there are updates
                notificationRepo.saveAll(notifications);
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("All unread notifications marked as read successfully");
            } else {
                // If no updates were needed
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("All notifications are already marked as read");
            }

            responseDTO.setContent(null);

        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO getUnreadNotificationCount(int user_id) {
        try {
            // Fetch the count of unread notifications for the given user_id
            long unreadCount = notificationRepo.countUnreadNotificationsByUserId(user_id);

            // Check if there are any unread notifications
            if (unreadCount > 0) {
                responseDTO.setStatusCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Unread notifications count retrieved successfully");
                responseDTO.setContent(unreadCount); // Returning the count as the content
            } else {
                responseDTO.setStatusCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No unread notifications found for the user");
                responseDTO.setContent(0); // No unread notifications
            }
        } catch (Exception e) {
            responseDTO.setStatusCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error while fetching unread notifications count: " + e.getMessage());
            responseDTO.setContent(null);
        }

        return responseDTO;
    }


}
