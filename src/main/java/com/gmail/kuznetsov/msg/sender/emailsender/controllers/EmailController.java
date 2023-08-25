package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext;
import com.gmail.kuznetsov.msg.sender.emailsender.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Контроллер отправки email
 */
@RestController
@RequestMapping("/admin/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService service;

    /**
     * Отправить email
     * @param email контекст, необходимый для отправки
     * @return статус и тело ответа
     */
    @PostMapping("/send")
    public ResponseEntity<String> send(@Valid @RequestBody EmailContext email) {
        return ResponseEntity.ok(service.send(email));
    }
}
