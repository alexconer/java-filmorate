package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setLastUpdate(rs.getTimestamp("last_update").toInstant());

        if (rs.getString("mpa_rating") != null) {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getLong("mpa_rating"));
            mpa.setName(rs.getString("mpa_name"));
            mpa.setDescription(rs.getString("mpa_description"));
            film.setMpa(mpa);
        }

        if (rs.getString("genre_ids") != null) {
            List<String> genreIds = List.of(rs.getString("genre_ids").split(","));
            List<String> genreNames = List.of(rs.getString("genre_names").split(","));

            Set<Genre> genres = new HashSet<>();
            for (int i = 0; i < genreIds.size(); i++) {
                Genre genre = new Genre();
                genre.setId(Long.parseLong(genreIds.get(i)));
                genre.setName(genreNames.get(i));
                genres.add(genre);
            }
            film.setGenres(genres);
        }
        return film;
    }
}
