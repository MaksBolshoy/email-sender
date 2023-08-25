package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.PathDto;
import com.gmail.kuznetsov.msg.sender.emailsender.services.loaders.pdf.PdfLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер загрузки pdf-файлов
 */
@RestController
@RequestMapping("/admin/pdf")
@RequiredArgsConstructor
public class PdfLoaderController {
    private final PdfLoader loader;

    /**
     * Загрузить pdf на ЯндексДиск
     * @param dto обертка для представления пути (откуда) в виде json.
     */
    @PostMapping
    public void load(@RequestBody PathDto dto) {
        loader.upload(dto.getPath());
    }

}
