package com.gmail.kuznetsov.msg.sender.emailsender.services;

import org.springframework.stereotype.Service;

/**
 * Общий интерфейс для классов-загрузчиков данных.
 */
@FunctionalInterface
@Service
public interface Loader {
    /**
     * Загрузить данные
     * @param path путь к данным
     */
    void upload(String path);
}
