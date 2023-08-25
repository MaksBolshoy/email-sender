package com.gmail.kuznetsov.msg.sender.emailsender.repositories.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Модель для хранения мета-информации, необходимой для получения токена авторизации
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
