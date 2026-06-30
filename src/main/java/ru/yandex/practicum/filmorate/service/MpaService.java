package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaRepository) {
        this.mpaStorage = mpaRepository;
    }

    public MPA getMpaById(Integer id) {
        return mpaStorage.getMpaById(id);
    }

    public List<MPA> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
