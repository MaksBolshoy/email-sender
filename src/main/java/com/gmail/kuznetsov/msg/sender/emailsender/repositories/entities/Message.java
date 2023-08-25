package com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Сущность сообщения для хранения в БД
 */
@Getter
@Setter
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Pattern(regexp = "^(([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.[a-z]{2,6})?$")
    private String emailTo;
    @NotNull
    @Column(columnDefinition = "text", length = 10485760)
    private String message;
    private String subject;
    private String filename;
    private String attachment;
    @NotNull
    private LocalDate sendDate;
    @NotNull
    private LocalTime sendTime;
    private String templateLocation;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status = MessageStatus.READY_TO_SEND;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", filename='" + filename + '\'' +
                ", attachment='" + attachment + '\'' +
                ", sendDate=" + sendDate +
                ", sendTime=" + sendTime +
                ", templateLocation='" + templateLocation + '\'' +
                ", status=" + status +
                '}';
    }
}
