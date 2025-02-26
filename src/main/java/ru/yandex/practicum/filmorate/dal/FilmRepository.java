package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private final static String FIND_ALL_QUERY = """
            SELECT f.*, mr.NAME AS mpa_name, mr.DESCRIPTION AS mpa_description,
            listagg(g.id, ',') WITHIN GROUP (ORDER BY g.ID) AS genre_ids,
            listagg(g.name, ',') WITHIN GROUP (ORDER BY g.ID) AS genre_names
            FROM FILM f
            LEFT JOIN MPA_RATING mr ON mr.ID = f.MPA_RATING
            LEFT JOIN FILM_GENRE fg ON fg.FILM_ID = f.ID
            LEFT JOIN GENRE g ON g.ID = fg.GENRE_ID
            LEFT JOIN FILM_LIKE fl ON fl.FILM_ID = f.ID
    """;
    private final static String GROUP_BY_QUERY = " GROUP BY f.id";
    private final static String INSERT_FILM_QUERY = """
        INSERT INTO film (name, description, release_date, duration, mpa_rating, last_update) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
    """;
    private final static String MERGE_FILM_QUERY = """
        MERGE INTO film (id, name, description, release_date, duration, mpa_rating, last_update) KEY(id) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
    """;
    private final static String INSERT_GENRE_QUERY = """
        INSERT INTO film_genre (film_id, genre_id, last_update) VALUES (?, ?, CURRENT_TIMESTAMP)
    """;
    private final static String DELETE_GENRES_QUERY = """
        DELETE FROM film_genre WHERE film_id = ?
    """;
    private final static String ADD_LIKE_QUERY = """
        MERGE INTO film_like (film_id, user_id, last_update) KEY(film_id, user_id) VALUES (?, ?, CURRENT_TIMESTAMP)
    """;
    private final static String DELETE_LIKE_QUERY = """
        DELETE FROM film_like WHERE film_id = ? AND user_id = ?
    """;
    private final static String FIND_POPULAR_QUERY = """
        ORDER BY COUNT(fl.user_id) DESC limit ?
    """;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY + GROUP_BY_QUERY);
    }

    public Optional<Film> findById(Long id) {
        return findOne(FIND_ALL_QUERY + " WHERE f.id = ?" + GROUP_BY_QUERY, id);
    }

    public Film insert(Film film) {
        Long id = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId());

        if (film.getGenres() != null) {
            delete(DELETE_GENRES_QUERY, id);
            film.getGenres().forEach(genre -> update(INSERT_GENRE_QUERY, id, genre.getId()));
        }

        film.setId(id);
        return film;
    }

    public Film update(Long id, Film film) {
        insert(
                MERGE_FILM_QUERY,
                id,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay().toInstant(ZoneOffset.UTC)),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId());

        if (film.getGenres() != null) {
            delete(DELETE_GENRES_QUERY, id);
            film.getGenres().forEach(genre -> update(INSERT_GENRE_QUERY, id, genre.getId()));
        }

        film.setId(id);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        update(ADD_LIKE_QUERY, filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        update(DELETE_LIKE_QUERY, filmId, userId);
    }

    public Collection<Film> getPopular(int limit) {
        return findMany(FIND_ALL_QUERY + GROUP_BY_QUERY + FIND_POPULAR_QUERY, limit);
    }
}
