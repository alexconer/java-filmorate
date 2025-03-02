package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRepository.class, FilmRowMapper.class})
public class FilmIntegrationTest {
    private final FilmDbStorage filmStorage;

    @Test
    public void testCreateFilm() {
        Mpa mpa = new Mpa(1L, "mpa", "desc");
        Genre genre1 = new Genre(1L, "genre1");
        Genre genre2 = new Genre(2L, "genre2");
        Film film = new Film(1L, "film", "desc", LocalDate.of(2000, 12, 28), 120, mpa, Set.of(genre1, genre2), Instant.now());

        Film newFilm = filmStorage.create(film);
        assertNotNull(newFilm);
    }

    @Test
    public void testGetFilm() {
        Mpa mpa = new Mpa(1L, "mpa", "desc");
        Genre genre1 = new Genre(1L, "genre1");
        Genre genre2 = new Genre(2L, "genre2");
        Film film = new Film(1L, "film", "desc", LocalDate.of(2000, 12, 28), 120, mpa, Set.of(genre1, genre2), Instant.now());
        filmStorage.create(film);

        Film newFilm = filmStorage.get(1L);
        assertNotNull(newFilm);
    }
}
