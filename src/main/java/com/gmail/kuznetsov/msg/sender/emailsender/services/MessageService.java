package com.gmail.kuznetsov.msg.sender.emailsender.services;

import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.ResourceNotFoundException;
import com.gmail.kuznetsov.msg.sender.emailsender.mappers.MessageMapper;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.MessageRepository;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Message;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports.MessageStatus;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext;
import com.gmail.kuznetsov.msg.sender.emailsender.services.loaders.pdf.PdfLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Сервис для работы с сущностями Message.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${spring.mail.sample.default}")
    private String defaultSample;

    @Value("${spring.mail.subject}")
    private String defaultSubject;

    @Value("${app.files.paths.temp}")
    private String pathToTempLoadDir;

    @Value("#{'${quotes}'.split(';')}")
    private List<String> quotes;

    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final PdfLoader loader;


    /**
     * Создать контекст-заготовку email для отправки по расписанию.
     * @param context DTO, содержащее контекст для email
     * @return context
     */
    public EmailContext createMessage(@Valid EmailContext context) {
        return messageMapper.toModel(createDefaultMessage(context));
    }

    /**
     * Получить случайную цитату
     * @return строка, случайная цитата
     */
    private String getRandomQuote() {
        return quotes.get(new Random().nextInt(0, quotes.size()));
    }

    /**
     * Получить модели EmailContext на основе всех сущностей Message из БД.
     * @return коллекция Message.
     */
    public List<EmailContext> getMessages() {
        return messageMapper.toModel(messageRepository.findAll());
    }

    /**
     * Получить модели EmailContext на основе всех сущностей Message из БД постранично.
     * @return коллекция Message.
     */
    public Page<EmailContext> getMessages(Integer page) {
        return messageRepository.findAll(PageRequest.of(page - 1, 8, Sort.by("id")))
                .map(messageMapper::toModel);
    }

    /**
     * Обновить статус сообщения
     * @param id идентификатор сообщения
     */
    public void updateMessageStatus(Long id) {
        Message message = getMessage(id);
        MessageStatus status = message.getStatus() == MessageStatus.SENT ?
                MessageStatus.READY_TO_SEND : MessageStatus.SENT;
        message.setStatus(status);
        messageRepository.save(message);
    }

    /**
     * Обновить статус сообщения
     * @param id идентификатор сообщения
     * @param status статус на который обновляем
     */
    public void updateMessageStatus(Long id, MessageStatus status) {
        Message message = getMessage(id);
        message.setStatus(status);
        messageRepository.save(message);
    }

    /**
     * Обновить статус сообщения
     * @param message сущность из БД
     * @param status статус на который обновляем
     */
    public void updateMessageStatus(Message message, MessageStatus status) {
        message.setStatus(status);
        messageRepository.save(message);
    }

    /**
     * Получить названия всех файлов-вложений
     * @return коллекция имен файлов-вложений
     */
    public Collection<String> getAttachments() {
        return loader.getAttachments();
    }

    /**
     * Создать заготовку для отсылки email.
     * @param context - метаинформация для отправки email.
     * @return сущность Message, сохраненная в БД.
     */
    private Message createDefaultMessage(EmailContext context) {
        Message message = new Message();
        message.setMessage(context.getMessage() == null ? getRandomQuote() : context.getMessage());
        message.setSubject(context.getSubject() == null ? defaultSubject : context.getSubject());
        message.setTemplateLocation(context.getTemplateLocation() == null ?
                defaultSample : context.getTemplateLocation());
        message.setSendDate(context.getSendDate());
        message.setSendTime(context.getSendTime());
        message.setFilename(context.getFilename());
        message.setAttachment(context.getAttachment() == null ?
                String.format("%s%s", pathToTempLoadDir, context.getFilename()) : context.getAttachment());
        if (context.getEmailTo() != null) message.setEmailTo(context.getEmailTo());
        return messageRepository.save(message);
    }

    /**
     * Получить сообщение из БД по id
     * @param id идентификатор сообщения
     * @return сущность Message
     */
    private Message getMessage(Long id) {
        return messageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Сообщение с id %s не найдено", id)
                )
        );
    }

    /**
     * Удалить все отправленные сообщения
     */
    public void deleteSentMessages() {
        Collection<Message> messages = getMessages().stream()
                .map(messageMapper::toMessage)
                .filter(m -> m.getStatus().equals(MessageStatus.SENT))
                .toList();
        messageRepository.deleteAll(messages);
    }

    /**
     * Удалить сообщение по id
     * @param id идентификатор сообщения
     */
    public void deleteMessageById(Long id) {
        Message message = getMessage(id);
        messageRepository.delete(message);
    }

    /**
     * Удалить сообщение по переданной модели
     * @param context переданная по сети модель сообщения
     */
    public void delete(EmailContext context) {
        messageRepository.delete(messageMapper.toMessage(context));
    }
}
