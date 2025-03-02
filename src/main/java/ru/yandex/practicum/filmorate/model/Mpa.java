package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {
    private Long id;
    @NotEmpty(message = "Наименование не может быть пустым")
    private String name;
    private String description;
}
