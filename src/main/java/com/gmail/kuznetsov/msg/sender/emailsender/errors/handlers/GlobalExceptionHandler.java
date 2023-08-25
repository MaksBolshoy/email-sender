package com.gmail.kuznetsov.msg.sender.emailsender.errors.handlers;

import com.gmail.kuznetsov.msg.sender.emailsender.errors.AppError;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.FieldsValidationError;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.*;
import com.yandex.disk.rest.exceptions.http.ConflictException;
import com.yandex.disk.rest.exceptions.http.NotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Обработчик исключений, возникающих в основном потоке выполнения программы.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> catchConflictException(ConflictException e) {
        return new ResponseEntity<>(
                new AppError(409, "Конфликт загрузки файла. " +
                        "Возможно такой файл уже был загружен на диск. Причины: \n" +
                        Arrays.toString(e.getStackTrace())),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(
                new AppError(404, "Файл не найден. Причины: \n" +
                        Arrays.toString(e.getStackTrace())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                new AppError(404,e.getMessage()), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<AppError> catchRemoveFileException(RemoveFileException e) {
        return new ResponseEntity<>(
                new AppError(409, e.getMessage()), HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchUserExistsException(UserExistsException e) {
        return new ResponseEntity<>(
                new AppError(409, e.getMessage()), HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchMessagingException(MessagingException e) {
        return new ResponseEntity<>(
                new AppError(400, "Ошибка отправки сообщения. Причины: " +
                        Arrays.toString(e.getStackTrace())), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchSendMessageException(SendMessageException e) {
        return new ResponseEntity<>(
                new AppError(400, "Ошибка отправки сообщения. Причины: " +
                        Arrays.toString(e.getStackTrace())), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchFileNotFoundException(FileNotFoundException e) {
        return new ResponseEntity<>(
                new AppError(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchFileIOException(FileIOException e) {
        return new ResponseEntity<>(
                new AppError(409, "Ошибка чтения/записи файла. Причины:  "
                        + Arrays.toString(e.getStackTrace())), HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<FieldsValidationError> catchMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(String.valueOf(e));
        List<String> errorMessages = new ArrayList<>(List.of(e.getMessage()));
        e.printStackTrace();
        return new ResponseEntity<>(new FieldsValidationError(errorMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<AppError> catchPSQLException(PSQLException e) {
        log.error(String.valueOf(e));
        return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), e.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> catchBadCredentialsException (
            BadCredentialsException  e
    ) {
        log.error(String.valueOf(e));
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<AppError> catchMalformedJwtException(MalformedJwtException e) {
        log.error(String.valueOf(e));
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }
}
