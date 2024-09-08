package com.example.eventix.service;

import com.example.eventix.entity.Message;
import com.example.eventix.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Fetch all messages that are not deleted
    public List<Message> getAllMessages() {
        return messageRepository.findByIsDeletedFalse();
    }

    public Message sendMessage(String content, String sender, String receiver) {
        Message message = new Message(content, sender, receiver, LocalDateTime.now(), true, false);
        return messageRepository.save(message);
    }

    // Utility method to get the current logged-in user's username
    private String getCurrentUsername() {
        // Retrieve the logged-in user's details from the SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }


    // Edit a message (check if it is editable)
    public Message editMessage(Long id, String newContent) {
        Message message = messageRepository.findById(id).orElseThrow();
        if (message.isEditable()) {
            message.setContent(newContent);
            message.setEditable(false); // Optional: Make it non-editable after the first edit
            return messageRepository.save(message);
        } else {
            throw new RuntimeException("Message cannot be edited");
        }
    }

    // Soft delete a message
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id).orElseThrow();
        message.setDeleted(true);
        messageRepository.save(message);
    }
}
