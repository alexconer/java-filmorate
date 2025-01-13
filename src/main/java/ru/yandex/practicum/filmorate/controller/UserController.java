package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.helper.CollectionHelper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Запрос списка пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрос добавления пользователя {}", user);

        if (!checkUserByEmail(user.getEmail())) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(CollectionHelper.getNextId(users));
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Запрос обновления пользователя {}", user);
        if (user.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь id = " + user.getId() + " не найден");
        }
        User oldUser = users.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().equals(oldUser.getEmail())) {
            if (!checkUserByEmail(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (oldUser.getLogin() != null) {
            oldUser.setLogin(user.getLogin());
        }
        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
        }
        users.put(user.getId(), oldUser);
        return oldUser;
    }

    private boolean checkUserByEmail(String email) {
        return !users.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
