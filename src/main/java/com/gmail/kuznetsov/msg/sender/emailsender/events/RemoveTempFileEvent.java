package com.gmail.kuznetsov.msg.sender.emailsender.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Событие удаления файла из папки временного хранения
 */
@Getter
public class RemoveTempFileEvent extends ApplicationEvent {
    private final String filename;

    public RemoveTempFileEvent(Object source, String filename) {
        super(source);
        this.filename = filename;
    }
}
