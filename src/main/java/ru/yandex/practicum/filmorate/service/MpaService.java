package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAll() {
        log.info("Запрос списка рейтингов");
        return mpaStorage.getAll();
    }

    public Mpa get(Long id) {
        log.info("Запрос рейтинга с id {}", id);

        if (mpaStorage.get(id) == null) {
            throw new NotFoundException("Рейтинг с id " + id + " не найден");
        }

        return mpaStorage.get(id);
    }
}
