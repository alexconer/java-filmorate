package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    /**
     * Возвращает коллекцию всех рейтингов
     */
    public Collection<Genre> getAll();

    /**
     * Возвращает рейтинг по его id
     */
    public Genre get(long id);
}
