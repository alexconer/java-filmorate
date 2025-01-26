package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public Collection<Film> getFilms() {
        log.info("Запрос списка фильмов");
        return filmStorage.getAll();
    }
    public Film createFilm(Film film) {
        log.info("Запрос добавления фильма {}", film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        log.info("Запрос обновления фильма {}", film);

        if (film.getId() == null) {
            throw new ValidationException("Не указан id фильма");
        }

        Film oldFilm = filmStorage.get(film.getId());
        if (oldFilm == null) {
            throw new NotFoundException("Фильм id = " + film.getId() + " не найден");
        }

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
        return filmStorage.update(film.getId(), oldFilm);
    }

}
