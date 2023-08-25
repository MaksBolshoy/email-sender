package com.gmail.kuznetsov.msg.sender.emailsender.repositories.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports.MessageStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Модель для хранения мета-информации, необходимой для отправки email
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmailContext implements Comparable<EmailContext> {
    private Long id;
    @NotNull
    @Email
    @Pattern(regexp = "^(([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.[a-z]{2,6})?$")
    private String emailTo;
    private String message;
    private String subject;
    private String filename;
    private String attachment;
    @NotNull
    private LocalDate sendDate;
    @NotNull
    private LocalTime sendTime;
    private String templateLocation;
    private Map<String, Object> context = new HashMap<>();
    @NotNull
    private MessageStatus status = MessageStatus.READY_TO_SEND;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailContext context = (EmailContext) o;
        return Objects.equals(emailTo, context.emailTo) && Objects.equals(message, context.message) && Objects.equals(filename, context.filename) && Objects.equals(sendDate, context.sendDate) && Objects.equals(sendTime, context.sendTime) && status == context.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailTo, message, filename, sendDate, sendTime, status);
    }

    @Override
    public String toString() {
        return String.format(
                "{ \"id\" : \"%s\", \"message\" : \"%s\", \"subject\" : \"%s\", \"filename\" : \"%s\", " +
                        "\"attachment\" : \"%s\", \"sendDate\" : \"%s\", \"sendTime\" : \"%s\", " +
                        "\"templateLocation\" : \"%s\", \"status\" : \"%s\"}",
                id, message, subject, filename, attachment, sendDate, sendTime, templateLocation, status);
    }

    @Override
    public int compareTo(EmailContext o) {
        LocalDateTime arg0 = LocalDateTime.of(this.sendDate, this.sendTime);
        LocalDateTime arg1 = LocalDateTime.of(o.getSendDate(), o.getSendTime());
        if (arg0.isAfter(arg1)) return 1;
        if (arg0.isBefore(arg1)) return -1;
        else return 0;
    }
}
