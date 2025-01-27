package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.helper.CollectionHelper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

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

    @Override
    public void addFriend(long id, long friendId) {
        // добаовляем в список друзей пользователя id
        friends.putIfAbsent(id, new HashSet<>());
        friends.get(id).add(friendId);

        // добавляем аналогичную ссылку для пользователя friendId
        friends.putIfAbsent(friendId, new HashSet<>());
        friends.get(friendId).add(id);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        if (friends.containsKey(id)) {
            friends.get(id).remove(friendId);
        }
        if (friends.containsKey(friendId)) {
            friends.get(friendId).remove(id);
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        if (!friends.containsKey(id)) {
            return Collections.emptyList();
        }
        return friends.get(id).stream()
                .map(users::get).toList();
    }

    @Override
    public Collection<User> getCommonFriends(long id, long otherId) {
        if (!friends.containsKey(id) || !friends.containsKey(otherId)) {
            return Collections.emptyList();
        }

        Set<Long> allOtherFriends = friends.get(otherId);
        return friends.get(id).stream()
                .filter(allOtherFriends::contains).map(users::get).toList();
    }
}
