package com.gmail.kuznetsov.msg.sender.emailsender.events.publishers;

import com.gmail.kuznetsov.msg.sender.emailsender.events.UploadToYandexDiskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Продюсер события загрузки файла на ЯндексДиск
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UploadToYandexDiskEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * публикация события
     * @param path путь к файлу
     */
    public void publishUploadToYandexDiskEvent(String path) {
        UploadToYandexDiskEvent event = new UploadToYandexDiskEvent(this, path);
        eventPublisher.publishEvent(event);
        log.debug("Upload file {} from temp storage directory to yandex disk", path);
    }
}
