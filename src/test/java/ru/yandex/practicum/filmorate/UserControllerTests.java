package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
    }

    @Test
    void testCreateUser() throws Exception {
        // корректный пользователь
        User user = new User(1L, "foo2@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isOk());

        // пользователь с некорректной почтой
        user = new User(2L, "fooyandexru", "foo", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());

        // пользователь с пустым логином
        user = new User(3L, "foo@yandex.ru", "", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());

        // пользователь с некорректным логином
        user = new User(4L, "foo@yandex.ru", "f o o", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());

        // пользователь с датой рождения из будущего
        user = new User(5L, "foo@yandex.ru", "foo", "bar", LocalDate.now().plusDays(1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());

        // пользователь с повторным email
        user = new User(6L, "foo@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isBadRequest());

        // корректный пользователь c пустым именем
        user = new User(7L, "baz@yandex.ru", "foo", null, LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isOk());

        Collection<User> users = userController.getUsers();
        assertEquals(3, users.size());

        Optional<User> user7 = users.stream().filter(u -> u.getId().equals(7L)).findFirst();
        if (user7.isPresent()) {
            assertEquals("foo", user7.get().getName());
        }
    }

    @Test
    void testUpdateUser() throws Exception {
        // корректный пользователь
        User user = new User(1L, "foo@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
        ).andExpect(status().isOk());

        // Пользовательс пустым id
        User newUser = new User(null, "foo@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser))
        ).andExpect(status().isBadRequest());

        // Пользователь с несуществующим id
        newUser = new User(2L, "foo@yandex.ru", "foo", "bar", LocalDate.of(2000, 1, 1));
        mockMvc.perform(
                put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser))
        ).andExpect(status().isNotFound());

        // Корректное обновление
        newUser = new User(1L, "foo@yandex.ru", "foo2", "bar2", LocalDate.of(2001, 1, 1));
        mockMvc.perform(
                put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser))
        ).andExpect(status().isOk());

        Collection<User> users = userController.getUsers();
        assertEquals(1, users.size());
        assertEquals(newUser, users.iterator().next());
    }
}
