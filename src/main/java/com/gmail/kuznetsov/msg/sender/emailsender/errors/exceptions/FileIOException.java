package com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions;

/**
 * Исключение при работе с файлом
 */
public class FileIOException extends RuntimeException {
    public FileIOException(String message) {
        super(message);
    }
}
