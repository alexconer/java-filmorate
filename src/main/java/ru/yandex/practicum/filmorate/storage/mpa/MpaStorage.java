package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    /**
     * Возвращает коллекцию всех рейтингов
     */
    public Collection<Mpa> getAll();

    /**
     * Возвращает рейтинг по его id
     */
    public Mpa get(long id);
}
