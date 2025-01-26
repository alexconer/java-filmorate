package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    /**
     * Возвращает коллекцию всех фильмов
     */
    public Collection<Film> getAll();

    /**
     * Создает новый фильм
     */
    public Film create(Film film);

    /**
     * Обновляет фильм
     */
    public Film update(long id, Film film);

    /**
     * Возвращает фильм по его id
     */
    public Film get(long id);
}
