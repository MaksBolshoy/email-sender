package com.gmail.kuznetsov.msg.sender.emailsender.services;

import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.FileIOException;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.ResourceNotFoundException;
import com.gmail.kuznetsov.msg.sender.emailsender.errors.exceptions.UserExistsException;
import com.gmail.kuznetsov.msg.sender.emailsender.events.publishers.CreateNewUserEventPublisher;
import com.gmail.kuznetsov.msg.sender.emailsender.mappers.UserMapper;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.UserRepository;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Role;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.User;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports.Roles;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.models.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы с сущностью User
 */
@Service
@RequiredArgsConstructor
public class UserService implements Loader, UserDetailsService {
    @Value("${app.files.contentType.txt}")
    private String txtContentType;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CreateNewUserEventPublisher publisher;
    private final RoleService roleService;

    /**
     * Добавить пользователя в БД.
     * @param dto - модель данных пользователя
     * @return - модель данных пользователя
     * @throws UserExistsException если такой пользователь уже есть в БД.
     */
    public UserDto addUser(UserDto dto) throws UserExistsException {
        String email = dto.getEmail();
        User user = userMapper.toEntity(dto);
        if (userRepository.findByEmail(email) != null) {
            throw new UserExistsException(String.format("Пользователь c email %s уже сохранен в базе данных", email));
        }
        user.setRoles(Collections.singleton(roleService.getByName(Roles.USER.name)));
        publisher.publishCreateNewUserEvent(email);
        return userMapper.toModel(userRepository.save(user));
    }

    /**
     * Записать множество пользователей в БД.
     * @param path - путь к файлу со списком email пользователей
     * @throws FileIOException при ошибке чтения/записи
     * @throws UserExistsException при невозможности корректно создать пользователя (например, невалидный email)
     */

    @Async
    @Override
    public void upload(String path) throws FileIOException, UserExistsException {
        Set<User> users = prepareUsers(path);

        for (User user : users) {
            String email = user.getEmail();
            if (userRepository.findByEmail(email) == null) {
                publisher.publishCreateNewUserEvent(email);
                userRepository.save(user);
            }
        }

    }

    /**
     * Подготовить множество пользователей для записи в БД.
     * @param path - путь к txt файлу со списком email пользователей
     * @return множество пользователей
     */
    private Set<User> prepareUsers(String path) {
        Set<User> users = new HashSet<>();
        File emails = Path.of(path.trim()).toFile();
        if (isTxt(emails)) {
            if (emails.isFile()) {
                try(BufferedReader reader = new BufferedReader(new FileReader(emails))) {
                    String email;
                    while ((email = reader.readLine()) != null) {
                        User user = new User();
                        user.setEmail(email);
                        user.setRoles(Collections.singleton(roleService.getByName(Roles.USER.name)));
                        users.add(user);
                    }
                    return users;
                } catch (FileNotFoundException e) {
                    throw new ResourceNotFoundException(String.format("Файл %s не найден", path));
                } catch (IOException e) {
                    throw new FileIOException("Ошибка чтения/записи файла");
                }
            } else {
                throw new UserExistsException(String.format("Невозможно прочитать email из файла %s", path));
            }
        } else {
            throw new UserExistsException(String.format("Невозможно прочитать email из файла %s", path));
        }
    }

    /**
     * Получить модели всех пользователей на основе сущностей из БД.
     * @return коллекция моделей пользователей.
     */
    public Collection<UserDto> getAllUsers() {
        return userMapper.toModel(userRepository.findAll());
    }

    /**
     * Получить одну из страниц с пользователями
     * @param page страница с пользователями
     * @return page
     */
    public Page<UserDto> getUsersPage(Integer page) {
        return userRepository.findAll(PageRequest.of(page - 1, 3, Sort.by("id")))
                .map(userMapper::toModel);
    }

    /**
     * Удалить пользователя по id
     * @param id уникальный идентификатор пользователя
     */
    public void delete(Long id) {
        User user = findUserById(id);
        Role admin = roleService.getByName(Roles.ADMIN.name);
        if (user.getRoles().contains(admin)) {
            throw new UserExistsException("Администратора нельзя удалить из БД!");
        }
        userRepository.delete(user);
    }

    /**
     * Найти пользователя по id
     * @param id уникальный идентификатор пользователя
     * @return найденный пользователь
     * @throws ResourceNotFoundException если пользователь с заданным id не найден
     */
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Пользователь с id %s не найден.", id)
                        )
                );
    }

    /**
     * При помощи "магических байтов" и библиотеки javax.activation определяет,
     * является ли файл txt-документом;
     * @param file - проверяемый файл;
     * @return - логическое: является ли файл txt-документом;
     */
    @SneakyThrows
    private boolean isTxt(File file) {
        final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String contentType = fileTypeMap.getContentType(file.getAbsolutePath());
        return contentType.equals(txtContentType);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), mapRolesToAuthorities(u.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(
                        role.getRoleName()))
                .collect(Collectors.toList());
    }

}
