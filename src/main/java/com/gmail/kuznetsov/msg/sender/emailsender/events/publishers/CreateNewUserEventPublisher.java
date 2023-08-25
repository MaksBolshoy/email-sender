package com.gmail.kuznetsov.msg.sender.emailsender.events.publishers;

import com.gmail.kuznetsov.msg.sender.emailsender.events.CreateNewUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Продюсер события создания нового пользователя.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNewUserEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Опубликовать событие создания нового пользователя.
     * @param email - email пользователя.
     */
    public void publishCreateNewUserEvent(String email) {
        CreateNewUserEvent event = new CreateNewUserEvent(this, email);
        eventPublisher.publishEvent(event);
        log.debug("Create new user with email {}", email);
    }
}
