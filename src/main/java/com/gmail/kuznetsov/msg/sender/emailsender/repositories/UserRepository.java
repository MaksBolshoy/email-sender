package com.gmail.kuznetsov.msg.sender.emailsender.repositories;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий сущностей пользователей.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Получить пользователя по email
     * @param email email
     * @return пользователь
     */
    User findByEmail(String email);
}
