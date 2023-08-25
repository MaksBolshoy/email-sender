package com.gmail.kuznetsov.msg.sender.emailsender.repositories.models;

import lombok.Data;

/**
 * Модель для хранения мета-информации, необходимой для создания токена авторизации
 */
@Data
public class JwtRequest {
    private String email;
    private String password;
}
