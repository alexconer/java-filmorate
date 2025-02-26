package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {

    private static final String FIND_ALL_QUERY = """
            SELECT mpa.* from mpa_rating mpa
    """;

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Mpa> findById(Long id) {
        return findOne(FIND_ALL_QUERY + " WHERE id = ?", id);
    }
}
