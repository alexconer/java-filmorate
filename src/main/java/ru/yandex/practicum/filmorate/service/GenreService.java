package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getAll() {
        log.info("Запрос списка жанров");
        return genreStorage.getAll();
    }

    public Genre get(Long id) {
        log.info("Запрос жанра с id {}", id);

        if (genreStorage.get(id) == null) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }

        return genreStorage.get(id);
    }
}
