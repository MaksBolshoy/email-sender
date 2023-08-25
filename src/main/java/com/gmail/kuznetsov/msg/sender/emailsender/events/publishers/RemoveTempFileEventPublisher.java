package com.gmail.kuznetsov.msg.sender.emailsender.events.publishers;

import com.gmail.kuznetsov.msg.sender.emailsender.events.RemoveTempFileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Продюсер создания события удаления файлов из временного хранилища
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveTempFileEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Публикация события
     * @param path путь к удаляемому файлу
     */
    public void publishRemoveTempFileEvent(String path) {
        RemoveTempFileEvent event = new RemoveTempFileEvent(this, path);
        eventPublisher.publishEvent(event);
        log.debug("Удален файл {} из директории временного хранения", path);
    }
}
