package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRepository.class, UserRowMapper.class})
public class UserIntegrationTest {
    private final UserDbStorage userStorage;

    @Test
    public void testCreateUser() {
        User user = new User(1L, "foo@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1), Instant.now());

        User newUser = userStorage.create(user);
        assertNotNull(newUser);
    }

    @Test
    public void testGetFilm() {
        User user = new User(1L, "foo@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1), Instant.now());
        userStorage.create(user);

        User newUser = userStorage.get(1L);
        assertNotNull(newUser);
    }
}
