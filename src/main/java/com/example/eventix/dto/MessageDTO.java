////package com.example.eventix.dto;
////
////public class MessageDTO {
////}
//package com.example.eventix.dto;
//
//import java.time.LocalDateTime;
//
//public class MessageDTO {
//
//    private Long id;
//    private String content;
//    private String sender;
//    private String receiver; // New field
//    private LocalDateTime timestamp;
//    private boolean isEditable; // New field
//    private boolean isDeleted; // New field
//
//    // Default constructor
//    public MessageDTO() {}
//
//    // Constructor with all fields
//    public MessageDTO(Long id, String content, String sender, String receiver, LocalDateTime timestamp, boolean isEditable, boolean isDeleted) {
//        this.id = id;
//        this.content = content;
//        this.sender = sender;
//        this.receiver = receiver;
//        this.timestamp = timestamp;
//        this.isEditable = isEditable;
//        this.isDeleted = isDeleted;
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getSender() {
//        return sender;
//    }
//
//    public void setSender(String sender) {
//        this.sender = sender;
//    }
//
//    public String getReceiver() {
//        return receiver;
//    }
//
//    public void setReceiver(String receiver) {
//        this.receiver = receiver;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public boolean isEditable() {
//        return isEditable;
//    }
//
//    public void setEditable(boolean editable) {
//        isEditable = editable;
//    }
//
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }
//}
package com.example.eventix.dto;

import java.time.LocalDateTime;

public class MessageDTO {

    private Long id;
    private String content;
    private String sender;
    private String receiver; // New field for the receiver
    private LocalDateTime timestamp;
    private boolean isEditable; // New field to track if the message is editable
    private boolean isDeleted; // New field to track if the message is deleted

    // Default constructor
    public MessageDTO() {}

    // Constructor with all fields
    public MessageDTO(Long id, String content, String sender, String receiver, LocalDateTime timestamp, boolean isEditable, boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.isEditable = isEditable;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // toString method for debugging and logging purposes
    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", timestamp=" + timestamp +
                ", isEditable=" + isEditable +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

