package com.gmail.kuznetsov.msg.sender.emailsender.events.listeners;

import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.RemoveFileException;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.ResourceNotFoundException;
import com.gmail.kuznetsov.msg.sender.emailsender.events.RemoveTempFileEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

/**
 * Слушатель события удаления файла из временной директории на сервере
 */
@Component
public class RemoveTempFileEventListener implements ApplicationListener<RemoveTempFileEvent> {
    @Value("${app.files.paths.temp}")
    private String pathToTempLoadDir;

    /**
     * Обработка события удаления
     * @param event - событие удаления файла из временной директории
     */
    @Override
    public void onApplicationEvent(RemoveTempFileEvent event) {
        remove(event.getFilename());
    }

    /**
     * Удаление файла из временной директории
     * @param path - путь к удаляемому файлу
     * @return логическое "удалился ли файл"
     */
    private boolean remove(String path) {
        File toRemove = Path.of(path).toFile();
        if (toRemove.exists()) {
            if (toRemove.isFile()) {
                boolean x = toRemove.delete();
                return toRemove.delete();
            } else {
                throw new RemoveFileException(String.format("Не удалось удалить файл %s", path));
            }
        } else throw new ResourceNotFoundException(String.format("Файл %s не найден", path));
    }
}
