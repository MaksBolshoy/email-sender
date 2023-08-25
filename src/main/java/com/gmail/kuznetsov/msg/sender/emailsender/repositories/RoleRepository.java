package com.gmail.kuznetsov.msg.sender.emailsender.repositories;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий сущностей ролей.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Получить роль по ее имени
     * @param name имя роли
     * @return роль
     */
    Role findByRoleName(String name);
}
