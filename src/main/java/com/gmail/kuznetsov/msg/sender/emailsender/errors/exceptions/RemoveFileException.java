package com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions;

/**
 * Исключение при удалении файла(например, из временного хранилища)
 */
public class RemoveFileException extends RuntimeException {

    public RemoveFileException(String message) {
        super(message);
    }
}
