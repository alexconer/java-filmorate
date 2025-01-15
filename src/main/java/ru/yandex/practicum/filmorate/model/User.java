package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Long id;

    @NotEmpty(message = "Email пользователя не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @NotEmpty(message = "Логин пользователя не может быть пустым")
    @Pattern(regexp = "^\\w*$", message = "Логин должен содержать только буквы, цифры и знак подчеркивания")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения не может быть позднее текущей")
    private LocalDate birthday;
}
