package com.gmail.kuznetsov.msg.sender.emailsender.repositories;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий сущностей сообщений.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findByFilename(String filename);
}
