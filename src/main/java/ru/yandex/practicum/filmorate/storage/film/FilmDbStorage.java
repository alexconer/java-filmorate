package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

@Component("FilmDBStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final FilmRepository filmRepository;

    @Override
    public Collection<Film> getAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film create(Film film) {
        return filmRepository.insert(film);
    }

    @Override
    public Film update(long id, Film film) {
        return filmRepository.update(id, film);
    }

    @Override
    public Film get(long id) {
        return filmRepository.findById(id).orElse(null);
    }

    @Override
    public void addLike(long id, long userId) {

    }

    @Override
    public void removeLike(long id, long userId) {

    }

    @Override
    public Set<Long> getLikes(long id) {
        return Set.of();
    }
}
