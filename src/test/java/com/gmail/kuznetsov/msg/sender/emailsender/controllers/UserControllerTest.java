package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.config.init.SettingsIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends SettingsIntegrationTest {

    @Test
    @SneakyThrows
    @Disabled("Если приветственное сообщение помечено YandexDisk как неудачное, тест упадет")
    public void addUserToDataBaseTest() {
        mvc
                .perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"example@fulwark.com\"}")
                )
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", hasKey("email")))
                .andExpect(jsonPath("$.email").value("example@fulwark.com"))
                .andExpect(jsonPath("$", hasKey("password")))
                .andExpect(jsonPath("$", hasKey("roles")))
                .andExpect(status().isOk()
                );
    }

    @Test
    @SneakyThrows
    public void addDuplicateUserToDataBaseTest() {
        mvc
                .perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"vajiro2902@fulwark.com\"}")
                )
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$", hasKey("statusCode")))
                .andExpect(jsonPath("$.statusCode").value(409))
                .andExpect(jsonPath("$", hasKey("message")))
                .andExpect(jsonPath("$.message")
                        .value("Пользователь c email vajiro2902@fulwark.com уже сохранен в базе данных"))
                .andExpect(status().isConflict()
                );

    }

    @Test
    @SneakyThrows
    public void addUsersToDataBaseTest() {
        String token = getToken();
        String path = String.format("src%stest%sresources%stestfiles%stestUsers.txt",
                SEPARATOR, SEPARATOR, SEPARATOR, SEPARATOR);
        String dto = String.format("{\"path\" : \"%s\"}", path);
        mvc
                .perform(post("/users/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(dto)
                )
                .andDo(print())
                .andExpect(jsonPath("$", hasToString("{result=Ok}")))
                .andExpect(status().isOk()
                );
    }

    @Test
    @SneakyThrows
    public void addUsersWithoutCredentialsToDataBaseTest() {
        mvc
                .perform(post("/users/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("testfiles/testUsers.txt")
                )
                .andDo(print())
                .andExpect(status().isForbidden()
                );
    }
}
