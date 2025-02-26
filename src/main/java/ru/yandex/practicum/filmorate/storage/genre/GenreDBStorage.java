package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {

    private final GenreRepository genreRepository;

    @Override
    public Collection<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre get(long id) {
        return genreRepository.findById(id).orElse(null);
    }
}
