//package com.example.eventix.service;
//
//public class MessageService {
//}
package com.example.eventix.service;

import com.example.eventix.entity.Message;
import com.example.eventix.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Create
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    // Read all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Read message by ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Update
    public Message updateMessage(Long id, Message messageDetails) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setContent(messageDetails.getContent());
        message.setSender(messageDetails.getSender());
        message.setTimestamp(messageDetails.getTimestamp());
        return messageRepository.save(message);
    }

    // Delete
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        messageRepository.delete(message);
    }
}
