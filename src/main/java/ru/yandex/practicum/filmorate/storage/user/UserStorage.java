package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {
    /**
     * Возвращает коллекцию всех пользователей
     */
    public Collection<User> getAll();

    /**
     * Создает нового пользователя
     */
    public User create(User user);

    /**
     * Обновляет пользователя
     */
    public User update(long id, User user);

    /**
     * Возвращает пользователя по его id
     */
    public User get(long id);
}
