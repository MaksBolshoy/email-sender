package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.config.init.SettingsIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AuthControllerTest extends SettingsIntegrationTest {

    @Test
    @SneakyThrows
    public void createAuthTokenTest() {
        mvc
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

                )
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", hasKey("token")))
                .andExpect(status().isOk()
                );
    }

    @Test
    @SneakyThrows
    public void createAuthTokenWithIncorrectPasswordTest() {
        mvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                    {
                                        "email" : "vajiro2902@fulwark.com",
                                        "password" : "dagse"
                                    }
                                """
                        )

                )
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", hasKey("statusCode")))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$", hasKey("message")))
                .andExpect(jsonPath("$.message").value("Некорректный email или пароль"))
                .andExpect(status().isUnauthorized()
                );
    }

    @Test
    @SneakyThrows
    public void createAuthTokenWithIncorrectEmailTest() {
        mvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                    {
                                        "email" : "ERRORvajiro2902@fulwark.com",
                                        "password" : "100"
                                    }
                                """
                        )

                )
                .andDo(print())
                .andExpect(status().isForbidden()
                );
    }
}
