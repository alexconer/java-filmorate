package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Set;

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

    /**
     * Добавляет лайк к фильму
     */
    public void addLike(long id, long userId);

    /**
     * Удаляет лайк у фильма
     */
    public void removeLike(long id, long userId);

    /**
     * Возвращает все лайки по фильму
     */
    public Collection<Film> getPopular(int limit);
}
