package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Set<Genre> getAllGenres() {
        return new HashSet<>(genreStorage.getAllGenres());
    }

    public Genre getGenreById(Integer id) {
        return genreStorage.getGenreById(id);
    }

}
