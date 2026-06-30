package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {

    MPA getMpaById(Integer id);

    List<MPA> getAllMpa();
}
