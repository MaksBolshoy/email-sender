package com.gmail.kuznetsov.msg.sender.emailsender.mappers;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Message;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Конвертер модели EmailContext в сущность для хранения в БД Message.
 */
@Component
public class MessageMapper {

    /**
     * Конвертировать список сущностей в список моделей
     * @param messages список сущностей
     * @return список моделей
     */
    public List<EmailContext> toModel(List<Message> messages) {
        List<EmailContext> result = new ArrayList<>();
        for (Message elem : messages) {
            result.add(toModel(elem));
        }
        return result;
    }

    /**
     * Конвертировать сущность из БД в модель для передачи по сети
     * @param message сущность из БД
     * @return модель для передачи по сети
     */
    public EmailContext toModel(Message message) {
        EmailContext result = new EmailContext();
        result.setId(message.getId());
        result.setStatus(message.getStatus());
        result.setMessage(message.getMessage());
        result.setTemplateLocation(message.getTemplateLocation());
        result.setSendTime(message.getSendTime());
        result.setSendDate(message.getSendDate());
        result.setEmailTo(message.getEmailTo());
        result.setAttachment(message.getAttachment());
        result.setFilename(message.getFilename());
        result.setSubject(message.getSubject());
        return result;
    }

    /**
     * Конвертировать модель для передачи по сети в сущность для хранения в БД
     * @param context модель
     * @return сущность
     */
    public Message toMessage(EmailContext context) {
        Message result = new Message();
        result.setId(context.getId());
        result.setStatus(context.getStatus());
        result.setMessage(context.getMessage());
        result.setTemplateLocation(context.getTemplateLocation());
        result.setSendTime(context.getSendTime());
        result.setSendDate(context.getSendDate());
        result.setEmailTo(context.getEmailTo());
        result.setAttachment(context.getAttachment());
        result.setFilename(context.getFilename());
        result.setSubject(context.getSubject());
        return result;
    }
}
