package com.gmail.kuznetsov.msg.sender.emailsender.services;

import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.FileIOException;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.ResourceNotFoundException;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.SendMessageException;
import com.gmail.kuznetsov.msg.sender.emailsender.mappers.MessageMapper;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports.MessageStatus;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.UserDto;
import com.gmail.kuznetsov.msg.sender.emailsender.services.loaders.yandex.YandexDiskLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Сервис отправки сообщений.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${app.files.paths.temp}")
    private String pathToTempLoadDir;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.sample.greeting}")
    private String greetingSample;

    @Value("${spring.mail.subject}")
    private String defaultSubject;

    @Value("#{'${quotes}'.split(';')}")
    private List<String> quotes;

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageMapper messageMapper;

    private final YandexDiskLoader loader;
    private final UserService userService;
    private final MessageService messageService;

    /**
     * Отправить новому пользователю приветствие;
     * @param userEmail - email нового пользователя
     */
    public void sendGreetingEmail(String userEmail) {
        send(createGreetingMessage(userEmail));
    }

    /**
     * Создать сообщение-приветствие
     * @return модель MessageDto с приветствием.
     */
    private EmailContext createGreetingMessage(@Valid String email) {
        EmailContext message = new EmailContext();
        message.setSubject(defaultSubject);
        message.setTemplateLocation(greetingSample);
        message.setSendDate(LocalDate.now());
        message.setSendTime(LocalTime.now());
        message.setMessage(getRandomQuote());
        message.setEmailTo(email);
        return message;
    }

    /**
     * Отправить email(метод обрабатывает возможные исключения из метода prepareEmail).
     * @param email - DTO, содержащее всю необходимую информацию для отправки email
     */
    @Async
    public String send(EmailContext email) {
        try {
            prepareEmail(email);
            messageService.updateMessageStatus(messageMapper.toMessage(email), MessageStatus.SENT);
            log.info("Пользователю {} успешно отправлено сообщение", email.getEmailTo());
            return "Сообщение отправлено";
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new SendMessageException();
        }
    }

    /**
     * Подготовить email к отправке
     * @param email - DTO, содержащее всю необходимую информацию для отправки email
     * @throws MessagingException - Базовый класс для всех исключений,
     * создаваемых классами обмена сообщениями.
     */
    private void prepareEmail(EmailContext email) throws MessagingException {
        fillEmailContext(email);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        Context context = new Context();
        context.setVariables(email.getContext());
        String emailContent = templateEngine.process(email.getTemplateLocation(), context);
        messageHelper.setFrom(username);
        messageHelper.setTo(email.getEmailTo());
        messageHelper.setSubject(email.getSubject());
        messageHelper.setText(emailContent, true);
        String filename = email.getFilename();

        if (filename == null) email.setAttachment(null);
        FileSystemResource file;
        String attachment = pathToTempLoadDir + filename;

        if (filename != null) {
            loader.upload(filename);
            file = getFile(attachment);
            messageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
        }
        tryToSend(message, email);
    }

    /**
     * Попробовать отправить сообщение
     * @param message MimeMessage
     * @param email вся метаинформация для сообщения
     */
    private void tryToSend(MimeMessage message, EmailContext email) {
        try {
            emailSender.send(message);
        } catch (MailSendException e) {
            try {
                System.err.println("Сообщение помечено сервисом яндекса, как спам");
                email.setSendDate(LocalDate.now().plusDays(1));
                email.setStatus(MessageStatus.READY_TO_SEND);
                System.err.println("Будет еще попытка отправки. Дата повторной отправки: " + email.getSendDate());
                messageService.createMessage(email);
                messageService.delete(email);
                throw new RuntimeException("Неудачное сообщение удалено");
            } catch (MailSendException ex) {
                System.err.println("Сообщение помечено сервисом яндекса, как спам повторно");
                email.setStatus(MessageStatus.SENT);
            }
        }
    }

    /**
     * Заполнить хэш EmailContext.context значениями по умолчанию
     * @param email - мета-информация, необходимая для отправки email
     */
    private void fillEmailContext(EmailContext email) {
        Map<String, Object> context = email.getContext();
        context.put("emailTo", email.getEmailTo());
        context.put("message", email.getMessage());
        context.put("subject", email.getSubject());
        context.put("sendDate", email.getSendDate());
        context.put("sendTime", email.getSendTime());
        context.put("filename", email.getFilename());
        context.put("attachment", email.getAttachment());
        context.put("templateLocation", email.getTemplateLocation());
    }

    /**
     * Получить случайную цитату
     * @return строка, случайная цитата
     */
    private String getRandomQuote() {
        return quotes.get(new Random().nextInt(0, quotes.size()));
    }

    /**
     * Проверить(каждый день, раз в минуту), есть ли сообщения для отправки.
     */
    @Scheduled(cron = "0 * * * * MON-SUN")
    public void check() {
        log.info("Проверка БД на наличие неотправленных сообщений");
        Collection<EmailContext> messages = messageService.getMessages();
        LocalDateTime now = LocalDateTime.now();

        for (EmailContext msg : messages) {
            if (!msg.getStatus().equals(MessageStatus.SENT)) {
                LocalDateTime sendTime = LocalDateTime.of(msg.getSendDate(), msg.getSendTime());
                if (now.isAfter(sendTime) || now.equals(sendTime)) {
                    if (msg.getEmailTo() != null) {
                        send(msg);
                    } else {
                        sendToAllUsers(msg);
                    }
                    messageService.updateMessageStatus(msg.getId(), MessageStatus.SENT);
                }
                clear();
            }
        }
    }

    /**
     * Разослать сообщение пользователям.
     * @param msg - контекст с метаинформацией для посылки email.
     */

    @Async
    private void sendToAllUsers(EmailContext msg) {
        prepare(msg);
        for (UserDto user : userService.getAllUsers()) {
            msg.setEmailTo(user.getEmail());
            send(msg);
            log.info("Пользователю {} успешно отправлено сообщение", msg.getEmailTo());
        }
    }

    /**
     * Загружает файл-вложение для письма в папку временного хранения на сервере.
     * @param path - путь к файлу на сервере
     * @return - файл-вложение
     */
    private FileSystemResource getFile(String path) {
        try {
            return new FileSystemResource(ResourceUtils.getFile(path));
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Файл по пути: %s не найден", path));
        }
    }

    /**
     * При необходимости добавить вложение к email, добавляет его.
     * @param msg - контекст с метаинформацией для посылки email.
     */
    private void prepare(EmailContext msg) {
        if (msg.getFilename() != null) loader.upload(msg.getFilename());
    }

    /**
     * Очистить временное хранилище файлов на сервере.
     */
    private void clear() {
        try {
            FileUtils.cleanDirectory(Path.of(pathToTempLoadDir).toFile());
            log.info("Временное хранилище файлов на сервере очищено");
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new FileIOException("Ошибка при очистке временного хранилища на сервере");
        }
    }
}