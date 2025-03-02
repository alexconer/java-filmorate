package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User>  {

    private static final String FIND_ALL_QUERY = """
            SELECT u.* from users u
    """;
    private static final String INSERT_USER_QUERY = """
        INSERT INTO users (name, login, email, birthday, last_update) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)
    """;
    private static final String MERGE_USER_QUERY = """
        MERGE INTO users (id, name, login, email, birthday, last_update) KEY(id) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
    """;
    private static final String ADD_FRIENDS_QUERY = """
        MERGE INTO user_friendship (user_id, friend_id, confirmed, last_update) KEY(user_id, friend_id)
        VALUES (?, ?, (SELECT count(*) FROM USER_FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?) > 0, CURRENT_TIMESTAMP)
    """;
    private static final String CONFIRM_FRIENDS_QUERY = """
        UPDATE user_friendship SET confirmed = true WHERE user_id = ? AND friend_id = ?
    """;
    private static final String DELETE_FRIENDS_QUERY = """
        DELETE FROM user_friendship WHERE user_id = ? AND friend_id = ?
    """;
    private static final String FIND_FRIENDS_QUERY = """
        JOIN user_friendship f ON u.id = f.friend_id
        WHERE f.user_id = ?
    """;
    private static final String FIND_COMMON_FRIENDS_QUERY = """
        JOIN user_friendship f ON u.id = f.friend_id
        WHERE f.friend_id in (?, ?)
    """;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(Long id) {
        return findOne(FIND_ALL_QUERY + " WHERE id = ?", id);
    }

    public User insert(User user) {
        Long id = insert(
                INSERT_USER_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Timestamp.from(user.getBirthday().atStartOfDay().toInstant(ZoneOffset.UTC)));

        user.setId(id);
        return user;
    }

    public User update(Long id, User user) {
        insert(
                MERGE_USER_QUERY,
                id,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Timestamp.from(user.getBirthday().atStartOfDay().toInstant(ZoneOffset.UTC)));

        user.setId(id);
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        updateWithCheck(ADD_FRIENDS_QUERY, userId, friendId, friendId, userId);
        update(CONFIRM_FRIENDS_QUERY, friendId, userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        delete(DELETE_FRIENDS_QUERY, userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        return findMany(FIND_ALL_QUERY + FIND_FRIENDS_QUERY, userId);
    }

    public Collection<User> getCommonFriends(long userId, long otherId) {
        return findMany(FIND_ALL_QUERY + FIND_COMMON_FRIENDS_QUERY, userId, otherId);
    }
}
