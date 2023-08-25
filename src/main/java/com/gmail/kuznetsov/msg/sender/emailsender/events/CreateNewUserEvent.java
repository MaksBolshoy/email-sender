package com.gmail.kuznetsov.msg.sender.emailsender.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Событие добавления нового пользователя в БД.
 */
@Getter
public class CreateNewUserEvent extends ApplicationEvent {
    private final String email;

    public CreateNewUserEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
