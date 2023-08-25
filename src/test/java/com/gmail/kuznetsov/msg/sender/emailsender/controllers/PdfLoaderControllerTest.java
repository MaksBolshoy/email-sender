package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.config.init.SettingsIntegrationTest;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.FileIOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
public class PdfLoaderControllerTest extends SettingsIntegrationTest {
    @Value("${app.files.paths.temp}")
    private String pathToTempLoadDir;

    @Test
    @SneakyThrows
    @Disabled("Если такой файл уже загружен на YandexDisk, тест упадет")
    public void loadPdfTest() {
        String token = getToken();
        String path = String.format("src%stest%sresources%stestfiles",
                SEPARATOR, SEPARATOR, SEPARATOR);
        mvc
                .perform(post("/admin/pdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(path)
                )
                .andDo(print())
                .andExpect(status().isOk()
                );
        clear();
    }

    @Test
    @SneakyThrows
    @Disabled("Если такой файл еще не загружен на YandexDisk, тест упадет")
    public void loadPdfTest0() {
        String token = getToken();
        String path = String.format("src%stest%sresources%stestfiles",
                SEPARATOR, SEPARATOR, SEPARATOR);
        mvc
                .perform(post("/admin/pdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(path)
                )
                .andDo(print())
                .andExpect(jsonPath("$", hasKey("statusCode")))
                .andExpect(jsonPath("$.statusCode").value(409))
                .andExpect(jsonPath("$", hasKey("message")))
                .andExpect(status().isConflict()
                );
        clear();
    }

    private void clear() {
        try {
            FileUtils.cleanDirectory(Path.of(pathToTempLoadDir).toFile());
            log.info("Временное хранилище файлов на сервере очищено");
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new FileIOException("Ошибка при очистке временного хранилища на сервере");
        }
    }
}
