package com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Сущность хранящая имя файла-вложения, для хранения в БД
 */
@Getter
@Setter
@Table(name = "filenames")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "filenames")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String filename;

    public Attachment(String filename) {
        this.filename = filename;
    }
}
