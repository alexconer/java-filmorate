package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MpaDBStorage implements MpaStorage {

    private final MpaRepository mpaRepository;

    @Override
    public Collection<Mpa> getAll() {
        return mpaRepository.findAll();
    }

    @Override
    public Mpa get(long id) {
        return mpaRepository.findById(id).orElse(null);
    }
}
