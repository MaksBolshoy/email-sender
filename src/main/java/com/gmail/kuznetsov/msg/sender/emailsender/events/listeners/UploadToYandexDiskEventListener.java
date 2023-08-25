package com.gmail.kuznetsov.msg.sender.emailsender.events.listeners;

import com.gmail.kuznetsov.msg.sender.emailsender.events.UploadToYandexDiskEvent;
import com.gmail.kuznetsov.msg.sender.emailsender.services.loaders.yandex.YandexDiskLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель события загрузки файла на YandexDisk
 */
@Component
@RequiredArgsConstructor
public class UploadToYandexDiskEventListener implements ApplicationListener<UploadToYandexDiskEvent> {
    private final YandexDiskLoader loader;

    /**
     * Обработка события загрузки файла на YandexDisk
     *
     * @param event - событие загрузки файла на YandexDisk
     */
    @Override
    public void onApplicationEvent(UploadToYandexDiskEvent event) {
        loader.uploadToYandexDisk(event.getFilename());
    }

}
