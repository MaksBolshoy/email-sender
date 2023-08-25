package com.gmail.kuznetsov.msg.sender.emailsender.mappers;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.User;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Конвертер модели UserDto в сущность для хранения в БД User.
 */
@Component
public class UserMapper {

    /**
     * Конвертировать список сущностей в список моделей
     * @param users список сущностей
     * @return список моделей
     */
    public List<UserDto> toModel(List<User> users) {
        List<UserDto> result = new ArrayList<>();
        for (User elem : users) {
            result.add(toModel(elem));
        }
        return result;
    }

    /**
     * Конвертировать модель для передачи по сети в сущность для хранения в БД
     * @param user сущность
     * @return модель
     */
    public UserDto toModel(User user) {
        UserDto result = new UserDto();
        result.setId(user.getId());
        result.setEmail(user.getEmail());
        result.setRoles(user.getRoles());
        return result;
    }

    /**
     * Конвертировать модель для передачи по сети в сущность для хранения в БД
     * @param dto модель
     * @return сущность
     */
    public User toEntity(UserDto dto) {
        User result = new User();
        result.setId(dto.getId());
        result.setEmail(dto.getEmail());
        result.setRoles(dto.getRoles());
        return result;
    }

}
