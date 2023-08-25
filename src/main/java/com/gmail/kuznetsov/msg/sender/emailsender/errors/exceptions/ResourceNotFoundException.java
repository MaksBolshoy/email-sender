package com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions;

/**
 * Исключение, возникающее при невозможности отыскать ресурс по заданным параметрам(например, пользователя по id)
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Ресурс не найден.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
