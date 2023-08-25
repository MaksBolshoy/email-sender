package com.gmail.kuznetsov.msg.sender.emailsender.services.loaders.pdf;

import com.gmail.kuznetsov.msg.sender.emailsender.events.publishers.UploadToYandexDiskEventPublisher;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.AttachmentRepository;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Attachment;
import com.gmail.kuznetsov.msg.sender.emailsender.services.Loader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис загрузки pdf-файлов с локальной машины.
 */
@Service
@RequiredArgsConstructor
public class PdfLoader implements Loader {

    @Value("${app.files.suffix.pdf}")
    private String pdfSuffix;

    @Value("${app.files.paths.temp}")
    private String pathToTempLoadDir;

    @Value("${app.files.contentType.pdf}")
    private String pdfContentType;

    private final UploadToYandexDiskEventPublisher eventPublisher;
    private final AttachmentRepository attachmentRepository;

    /**
     * Загружает pdf документы с локальной машины на сервер;
     * @param path - путь к директории на локальной машине;
     */
    @Async
    @Override
    @SneakyThrows
    public void upload(String path) {
        List<MultipartFile> multipartFiles = toMultipartFiles(path);
        for (MultipartFile file: multipartFiles) {
            file.transferTo(Path.of(pathToTempLoadDir + file.getName()));
            attachmentRepository.save(new Attachment(file.getName()));
        }
        publishUploadEvent();
    }

    /**
     * Получить названия всех файлов-вложений
     * @return коллекция имен файлов-вложений
     */
    public Collection<String> getAttachments() {
        return attachmentRepository.findAll().stream()
                .map(Attachment::getFilename).collect(Collectors.toSet());
    }

    /**
     * Создаёт событие загрузки файлов на YandexDisk
     */
    private void publishUploadEvent() {
        for (File file : Objects.requireNonNull(Path.of(pathToTempLoadDir).toFile().listFiles())) {
            eventPublisher.publishUploadToYandexDiskEvent(file.getPath());
        }
    }

    /**
     * Подготавливает список org.springframework.web.multipart.MultipartFile для записи на сервер;
     * @param pathToLoadDir - путь к директории с загружаемыми файлами на локальной машине;
     * @return список org.springframework.web.multipart.MultipartFile;
     */
    private List<MultipartFile> toMultipartFiles(String pathToLoadDir) {
        List<File> files = getFiles(pathToLoadDir);
        List<MultipartFile> result = new ArrayList<>();
        for (File file : files) {
            CommonsMultipartFile multipartFile = new CommonsMultipartFile(createFileItem(file));
            result.add(multipartFile);
        }
        return result;
    }

    /**
     * Создает на основе java.io.File -> org.apache.commons.fileupload.disk.DiskFileItem;
     * @param file - файл для конвертации в org.apache.commons.fileupload.FileItem;
     * @return реализация FileItem -> org.apache.commons.fileupload.disk.DiskFileItem;
     */
    @SneakyThrows
    private FileItem createFileItem(File file) {
        String name = file.getName();
        if (!name.endsWith(pdfSuffix)) {
            name = name + pdfSuffix;
        }
        FileItem fileItem = new DiskFileItem(
                name, Files.probeContentType(file.toPath()),
                false, name, (int) file.length(), new File(pathToTempLoadDir + name));
        try(InputStream in = new FileInputStream(file);
            OutputStream out = fileItem.getOutputStream()
        ) {
            IOUtils.copy(in, out);
        }
        return fileItem;
    }

    /**
     * Возвращает список pdf-документов, найденных в указанной директории;
     * @param path - путь к директории;
     * @return список pdf-документов;
     */
    private List<File> getFiles(String path) {
        Path p = Path.of(path);
        File dir = p.toFile();
        if (dir.isFile()) {
            throw new RuntimeException("Передайте путь к папке, содержащей необходимые pdf");
        }
        File[] files = dir.listFiles();
        if (files == null) {
            throw new NullPointerException("Такой директории не существует");
        }
        return Arrays.stream(files).filter(this::isPdf).collect(Collectors.toList());
    }

    /**
     * При помощи "магических байтов" и библиотеки javax.activation определяет,
     * является ли файл pdf-документом;
     * @param file - проверяемый файл;
     * @return - логическое: является ли файл pdf-документом;
     */
    @SneakyThrows
    private boolean isPdf(File file) {
        final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String contentType = fileTypeMap.getContentType(file.getAbsolutePath());
        return contentType.equals(pdfContentType);
    }
}
