package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Component("UserDBStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final UserRepository userRepository;

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        return userRepository.insert(user);
    }

    @Override
    public User update(long id, User user) {
        return userRepository.update(id, user);
    }

    @Override
    public User get(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void addFriend(long id, long friendId) {
        userRepository.addFriend(id, friendId);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        userRepository.removeFriend(id, friendId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        return userRepository.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(long id, long otherId) {
        return List.of();
    }
}
