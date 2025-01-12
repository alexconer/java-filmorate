package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.helper.CollectionHelper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрос списка фильмов");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Запрос добавления фильма " + film);
        film.setId(CollectionHelper.getNextId(films));
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос обновления фильма " + film);
        if (film.getId() == null) {
            throw new ValidationException("Не указан id фильма");
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм id = " + film.getId() + "не найден");
        }
        Film oldFilm = films.get(film.getId());
        if (film.getName() != null) {
            oldFilm.setName(film.getName());
        }
        if (film.getDescription() != null) {
            oldFilm.setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
        }
        films.put(film.getId(), oldFilm);
        return oldFilm;
    }
}
