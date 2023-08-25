package com.gmail.kuznetsov.msg.sender.emailsender.errors.handlers;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Обработчик исключений, возникающих при асинхронном выполнении методов
 */
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.err.println("Ошибка: - " + throwable.getMessage());
        System.err.println("В методе: - " + method.getName());
        for (Object param : obj) {
            System.err.println("При переданных параметрах: - " + param);
        }
    }
}
