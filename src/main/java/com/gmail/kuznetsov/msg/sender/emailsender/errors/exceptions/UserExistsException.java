package com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions;

/**
 * Исключение, возникающее при создании пользователя
 */
public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("Пользователь уже сохранен в базе данных");
    }

    public UserExistsException(String message) {
        super(message);
    }
}
