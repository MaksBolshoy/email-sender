package com.gmail.kuznetsov.msg.sender.emailsender.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, обертка, для возникающих в приложении исключений, связанных с валидацией полей.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldsValidationError {
    private List<String> errorFieldsMessages;
}
