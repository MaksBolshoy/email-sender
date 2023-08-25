package com.gmail.kuznetsov.msg.sender.emailsender.controllers;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.PathDto;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.UserDto;
import com.gmail.kuznetsov.msg.sender.emailsender.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Контроллер для работы с пользователями
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Получить страницу с пользователями
     * @param page страница с пользователями
     * @return page
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam(name = "page", defaultValue = "1") Integer page) {
        if (page < 1) page = 1;
        return ResponseEntity.ok(userService.getUsersPage(page));
    }


    /**
     * Зарегистрировать нового пользователя
     * @param dto описание нового пользователя
     * @return статус и тело ответа
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.addUser(dto));
    }

    /** Зарегистрировать новых пользователей по списку email из файла
     * @param path путь к файлу с email в виде обертки json
     * @return статус и тело ответа
     */
    @PostMapping("/admin/register")
    public ResponseEntity<String> register(@Valid @RequestBody PathDto path) {
        userService.upload(path.getPath());
        return ResponseEntity.ok("{\"result\": \"Ok\"}");
    }

    /**
     * Удалить пользователя
     * @param id идентификатор пользователя
     */
    @DeleteMapping("/admin/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.delete(id);
    }
}
