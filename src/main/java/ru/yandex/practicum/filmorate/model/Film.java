package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.MinimumDate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotEmpty(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма должно быть не более 200 символов")
    private String description;
    @MinimumDate(message = "Некорректная дата выхода фильма")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма должна быть положительным числом")
    private Integer duration;
}
