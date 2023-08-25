package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.config.init.SettingsIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class EmailControllerTest extends SettingsIntegrationTest {
    @Test
    @SneakyThrows
    public void sendEmailTest() {
        String token = getToken();
        mvc
                .perform(post("/admin/emails/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content("""
                                    {
                                        "emailTo" : "vajiro2902@fulwark.com",
                                        "attachment": "src/test/resources/testfiles/cryptographic.pdf",
                                        "templateLocation" : "src/test/resources/testfiles/default.html",
                                        "sendDate" : "2023-06-24",
                                        "sendTime" : "12:04"
                                    }
                                """
                        )

                )
                .andDo(print())
                .andExpect(status().isOk()
                );
    }

    @Test
    @SneakyThrows
    public void sendEmailWithoutEmailToTest() {
        String token = getToken();
        mvc
                .perform(post("/admin/emails/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content("""
                                    {
                                        "attachment": "src/test/resources/testfiles/cryptographic.pdf",
                                        "templateLocation" : "src/test/resources/testfiles/default.html",
                                        "sendDate" : "2023-06-24",
                                        "sendTime" : "12:04"
                                    }
                                """
                        )

                )
                .andDo(print())
                .andExpect(jsonPath("$", hasKey("errorFieldsMessages")))
                .andExpect(jsonPath("$.errorFieldsMessages[0]")
                        .value("Validation failed for argument [0] in public org.springframework.http.ResponseEntity<java.lang.String> com.gmail.kuznetsov.msg.sender.emailsender.controllers.EmailController.send(com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext): [Field error in object 'emailContext' on field 'emailTo': rejected value [null]; codes [NotNull.emailContext.emailTo,NotNull.emailTo,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [emailContext.emailTo,emailTo]; arguments []; default message [emailTo]]; default message [must not be null]] "))
                .andExpect(status().is4xxClientError()
                );
    }

    @Test
    @SneakyThrows
    public void sendEmailWithNotTemplateTest() {
        String token = getToken();
        mvc
                .perform(post("/admin/emails/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content("""
                                    {
                                        "emailTo" : "vajiro2902@fulwark.com",
                                        "attachment": "src/test/resources/testfiles/cryptographic.pdf",
                                        "templateLocation" : "src/test/resources/testfiles/default.html",
                                        "sendTime" : "12:04"
                                    }
                                """
                        )

                )
                .andDo(print())
                .andExpect(jsonPath("$", hasKey("errorFieldsMessages")))
                .andExpect(jsonPath("$.errorFieldsMessages[0]")
                        .value("Validation failed for argument [0] in public org.springframework.http.ResponseEntity<java.lang.String> com.gmail.kuznetsov.msg.sender.emailsender.controllers.EmailController.send(com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.EmailContext): [Field error in object 'emailContext' on field 'sendDate': rejected value [null]; codes [NotNull.emailContext.sendDate,NotNull.sendDate,NotNull.java.time.LocalDate,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [emailContext.sendDate,sendDate]; arguments []; default message [sendDate]]; default message [must not be null]] "))
                .andExpect(status().is4xxClientError()
                );
    }
}
