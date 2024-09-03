//package com.example.eventix.repository;
//
//public interface MessageRepository {
//}
package com.example.eventix.repository;

import com.example.eventix.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
