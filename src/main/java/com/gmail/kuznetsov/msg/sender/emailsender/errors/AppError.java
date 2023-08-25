package com.gmail.kuznetsov.msg.sender.emailsender.errors;

import lombok.*;

/**
 * Класс, обертка, для возникающих в приложении исключений.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppError {
    private int statusCode;
    private String message;
}
