package com.gmail.kuznetsov.msg.sender.emailsender.services.loaders.yandex;

import com.gmail.kuznetsov.msg.sender.emailsender.events.publishers.RemoveTempFileEventPublisher;
import com.gmail.kuznetsov.msg.sender.emailsender.services.Loader;
import com.yandex.disk.rest.DownloadListener;
import com.yandex.disk.rest.ProgressListener;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.Link;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Класс-обертка для yandex-api
 */
@Service
@RequiredArgsConstructor
public class YandexDiskLoader implements Loader {
    @Value("${app.cloud.yandex.storage.pdf}")
    private String pathToLoadDir;

    @Value("${app.files.paths.temp}")
    private String pathToTempLoadDir;

    private final RestClient restClient;
    private final ProgressListener progressListener;
    private final RemoveTempFileEventPublisher publisher;

    /**
     * Загрузить файлы из временной папки на YandexDisk.
     *
     * @param pathToLocalFile - имя загружаемого файла.
     */
    @SneakyThrows
    public void uploadToYandexDisk(String pathToLocalFile) {
        Link link = getUploadLink(pathToLocalFile);
        Path path = Path.of(pathToLocalFile);
        restClient.uploadFile(
                link, true, path.toFile(), progressListener);
        publisher.publishRemoveTempFileEvent(path.toString());
    }

    /**
     * Загрузить файл с YandexDisk в папку временного хранения.
     *
     * @param filename - имя загружаемого файла
     */
    @Override
    @SneakyThrows
    public void upload(String filename) {
        String path = buildPath(pathToTempLoadDir, filename);
        File local;
        if (Path.of(path).toFile().exists()) {
            local = Path.of(path).toFile();
        } else {
            local = new File(path);
        }

        restClient.downloadFile(buildPath(pathToLoadDir, filename), new DownloadListener() {
            @Override
            public OutputStream getOutputStream(boolean append) throws IOException {
                return new FileOutputStream(local, append);
            }
        });

    }

    /**
     * Получить от yandex-api ссылку для хранения файла.
     *
     * @param pathToTempFile - путь к файлу на локальной машине для сохраняемого файла
     * @return ссылка для хранения файла на YandexDisk
     */
    @SneakyThrows
    private Link getUploadLink(String pathToTempFile) {
        return restClient.getUploadLink(buildPath(pathToLoadDir, Path.of(pathToTempFile).toFile().getName()), false);
    }

    /**
     * Составить строковое представление пути к файлу.
     *
     * @param prefix - путь к родительской директории.
     * @param suffix - имя файла.
     * @return путь к файлу.
     */
    private String buildPath(String prefix, String suffix) {
        return prefix.concat(suffix);
    }
}
