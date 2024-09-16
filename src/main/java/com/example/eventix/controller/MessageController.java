package com.example.eventix.controller;

import com.example.eventix.entity.Message;
import com.example.eventix.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

   //  Get all non-deleted messages
    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

//    @GetMapping("/conversation")
//    public List<Message> getMessages(@RequestParam String sender, @RequestParam String receiver) {
//        return messageService.getConversationBetweenUsers(sender, receiver);
//    }


    // Send a new message
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message.getContent(),message.getSender(), message.getReceiver());
    }

    // Edit a message
    @PutMapping("/edit/{id}")
    public Message editMessage(@PathVariable Long id, @RequestBody String newContent) {
        return messageService.editMessage(id, newContent);
    }

    // Soft delete a message
    @DeleteMapping("/delete/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
}

