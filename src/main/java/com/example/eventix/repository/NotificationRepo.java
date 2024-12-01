package com.example.eventix.repository;

import com.example.eventix.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n.student.id = :userId")
    List<Notification> findByUserId(@Param("userId") int userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.student.id = :userId AND n.is_read = false")
    long countUnreadNotificationsByUserId(@Param("userId") int userId);

}
