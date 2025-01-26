package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.helper.CollectionHelper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

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
}
