package com.gmail.kuznetsov.msg.sender.emailsender.config.init;

import com.gmail.kuznetsov.msg.sender.emailsender.config.IntegrationBaseTest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import java.nio.file.FileSystems;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
public class SettingsIntegrationTest extends IntegrationBaseTest {
    @Autowired
    protected MockMvc mvc;

    protected final String SEPARATOR = FileSystems.getDefault().getSeparator();

    @SneakyThrows
    protected String getToken() {
        MockHttpServletResponse response = mvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                    {
                                        "email" : "vajiro2902@fulwark.com",
                                        "password" : "100"
                                    }
                                """
                        )

                ).andReturn().getResponse();
        String temp = response.getContentAsString().split(":\"")[1];
        return "Bearer " + temp.substring(0, temp.length() - 2);
    }


    @PostConstruct
    private void fillDataBase() {

    }
}
