package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        log.info("Запрос списка пользователей");
        return userStorage.getAll();
    }

    public User createUser(User user) {
        log.info("Запрос добавления пользователя {}", user);

        if (!checkUserByEmail(user.getEmail())) {
            throw new ValidationException("Пользователь с таким email уже существует");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userStorage.create(user);
    }

    public User updateUser(User user) {
        log.info("Запрос обновления пользователя {}", user);
        if (user.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }

        User oldUser = userStorage.get(user.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь id = " + user.getId() + " не найден");
        }

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
        return userStorage.update(user.getId(), oldUser);
    }

    private boolean checkUserByEmail(String email) {
        return userStorage.getAll().stream().noneMatch(u -> u.getEmail().equals(email));
    }
}
