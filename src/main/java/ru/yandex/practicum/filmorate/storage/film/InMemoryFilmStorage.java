package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.helper.CollectionHelper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(CollectionHelper.getNextId(films));
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(long id, Film film) {
        films.put(id, film);
        return film;
    }

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public void addLike(long id, long userId) {
        likes.putIfAbsent(id, new HashSet<>());
        likes.get(id).add(userId);
    }

    @Override
    public void removeLike(long id, long userId) {
        if (likes.containsKey(id)) {
            likes.get(id).remove(userId);
        }
    }

    @Override
    public Set<Long> getLikes(long id) {
        return likes.getOrDefault(id, Collections.emptySet());
    }
}
