package com.gmail.kuznetsov.msg.sender.emailsender.repositories;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для хранения json-оберток для имен файлов-вложений
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
