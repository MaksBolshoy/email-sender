package com.gmail.kuznetsov.msg.sender.emailsender.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Модель события загрузки файла на YandexDisk
 */
@Getter
public class UploadToYandexDiskEvent extends ApplicationEvent {
    private final String filename;

    public UploadToYandexDiskEvent(Object source, String filename) {
        super(source);
        this.filename = filename;
    }
}
