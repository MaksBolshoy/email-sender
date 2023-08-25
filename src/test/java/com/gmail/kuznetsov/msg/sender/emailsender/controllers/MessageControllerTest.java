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
public class MessageControllerTest extends SettingsIntegrationTest {
    @Test
    @SneakyThrows
    public void createMessageTest() {
        String token = getToken();
        mvc
                .perform(post("/admin/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content("""
                                    {
                                        "filename" : "cryptographic.pdf",
                                        "attachment": "src/test/resources/testfiles/cryptographic.pdf",
                                        "sendDate" : "2023-06-24",
                                        "sendTime" : "12:04"
                                    }
                                """
                        )
                )
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", hasKey("id")))
                .andExpect(jsonPath("$", hasKey("emailTo")))
                .andExpect(jsonPath("$", hasKey("message")))
                .andExpect(jsonPath("$", hasKey("attachment")))
                .andExpect(jsonPath("$.attachment").value("src/test/resources/testfiles/cryptographic.pdf"))
                .andExpect(jsonPath("$", hasKey("filename")))
                .andExpect(jsonPath("$.filename").value("cryptographic.pdf"))
                .andExpect(jsonPath("$", hasKey("sendDate")))
                .andExpect(jsonPath("$.sendDate").value("2023-06-24"))
                .andExpect(jsonPath("$", hasKey("sendTime")))
                .andExpect(jsonPath("$.sendTime").value("12:04:00"))
                .andExpect(jsonPath("$", hasKey("status")))
                .andExpect(jsonPath("$.status").value("READY_TO_SEND"))
                .andExpect(status().isOk()
                );
    }
}
