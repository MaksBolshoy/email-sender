package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext;
import com.gmail.kuznetsov.msg.sender.emailsender.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер работы с сообщениями
 */
@RestController
@RequestMapping("/admin/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    /**
     * Создать сообщение
     * @param context мета информация для создания email
     * @return статус и тело ответа
     */
    @PostMapping
    public ResponseEntity<EmailContext> create(@RequestBody EmailContext context) {
        return ResponseEntity.ok(messageService.createMessage(context));
    }

    /**
     * Плучить названия всех файлов-вложений, хранимых в облаке
     * @return коллекция файлов
     */
    @GetMapping("/files")
    public ResponseEntity<Collection<String>> getAttachments() {
        return ResponseEntity.ok(messageService.getAttachments());
    }

    /**
     * Получить страницу с моделями сообщений
     * @param page страница сообщений
     * @return page
     */
    @GetMapping
    public Page<EmailContext> getMessages(@RequestParam(name = "page", defaultValue = "1") Integer page) {
        if (page < 1) page = 1;
        return messageService.getMessages(page);
    }

    /**
     * Обновить статус сообщения
     * @param id идентификатор сообщения
     */
    @PatchMapping("{id}")
    public void updateMessageStatus(@PathVariable Long id) {
        messageService.updateMessageStatus(id);
    }

    /**
     * Удалить все отправленные сообщения
     */
    @DeleteMapping
    public void deleteSentMessages() {
        messageService.deleteSentMessages();
    }

    /**
     * Удалить сообщения по id
     * @param id идентификатор сообщения
     */
    @DeleteMapping("{id}")
    public void deleteSentMessages(@PathVariable Long id) {
        messageService.deleteMessageById(id);
    }
}



