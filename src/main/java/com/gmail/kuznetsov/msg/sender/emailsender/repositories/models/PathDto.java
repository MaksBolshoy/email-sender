package com.gmail.kuznetsov.msg.sender.emailsender.repositories.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Модель для хранения пути к файлу и представления его в виде json.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PathDto {
    @NotNull
    private String path;
}
