package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

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

    public Film getFilm(Long id) {
        log.info("Запрос фильма id = {}", id);
        Film film = filmStorage.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм id = " + id + " не найден");
        }
        return film;
    }

    public void addLike(Long id, Long userId) {
        log.info("Добавления лайка к фильму id = {} пользователем id = {}", id, userId);
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм id = " + id + " не найден");
        }
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("Пользователь id = " + userId + " не найден");
        }
        filmStorage.addLike(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        log.info("Удаления лайка к фильму id = {} пользователем id = {}", id, userId);
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм id = " + id + " не найден");
        }
        if (userStorage.get(userId) == null) {
            throw new NotFoundException("Пользователь id = " + userId + " не найден");
        }
        filmStorage.removeLike(id, userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        log.info("Запрос популярных фильмов");
        if (count == null || count < 0) {
            count = 10;
        }
        return filmStorage.getPopularFilms(count);
    }

}
