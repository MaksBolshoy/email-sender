package com.gmail.kuznetsov.msg.sender.emailsender.events.listeners;

import com.gmail.kuznetsov.msg.sender.emailsender.events.CreateNewUserEvent;
import com.gmail.kuznetsov.msg.sender.emailsender.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель события создания нового пользователя
 */
@Component
@RequiredArgsConstructor
public class CreateNewUserEventListener implements ApplicationListener<CreateNewUserEvent> {
    private final EmailService service;

    /**
     * Обработка события
     * @param event событие создания нового пользователя
     */
    @Override
    public void onApplicationEvent(CreateNewUserEvent event) {
        service.sendGreetingEmail(event.getEmail());
    }
}
