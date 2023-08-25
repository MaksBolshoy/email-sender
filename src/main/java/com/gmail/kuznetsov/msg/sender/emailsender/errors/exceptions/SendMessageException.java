package com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions;

/**
 * Исключение отправки сообщения
 */
public class SendMessageException extends RuntimeException {

    public SendMessageException() {
        super("Непредвиденная ошибка отправки сообщения.");
    }

    public SendMessageException(String message) {
        super(message);
    }
}
