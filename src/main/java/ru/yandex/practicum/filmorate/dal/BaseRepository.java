package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    private final JdbcTemplate jdbc;
    private final RowMapper<T> mapper;

    protected Optional<T> findOne(String sql, Object... args) {
        try {
            T result = jdbc.queryForObject(sql, mapper, args);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected Collection<T> findMany(String sql, Object... args) {
        return jdbc.query(sql, mapper, args);
    }

    protected boolean delete(String sql, Object... args) {
        return jdbc.update(sql, args) > 0;
    }

    protected void updateWithCheck(String sql, Object... args) {
        int result = jdbc.update(sql, args);
        if (result == 0) {
            throw new InternalServerException("Ошибка при обновлении");
        }
    }

    protected void update(String sql, Object... args) {
        jdbc.update(sql, args);
    }

    protected <V> void batchUpdate(String sql, List<List<V>> argsList) {
        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                for (int j = 0; j < argsList.get(i).size(); j++) {
                    ps.setObject(j + 1, argsList.get(i).get(j));
                }
            }

            @Override
            public int getBatchSize() {
                return argsList.size();
            }
        });
    }

    protected Long insert(String sql, Object... args) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id == null) {
            throw new InternalServerException("Ошибка при добавлении");
        } else {
            return id;
        }
    }
}
