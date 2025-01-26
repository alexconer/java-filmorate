package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.helper.CollectionHelper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(CollectionHelper.getNextId(users));
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(long id, User user) {
        users.put(id, user);
        return user;
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }
}
