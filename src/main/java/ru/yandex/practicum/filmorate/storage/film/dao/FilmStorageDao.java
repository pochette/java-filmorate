package ru.yandex.practicum.filmorate.storage.film.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

public class FilmStorageDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmStorageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Long> addLike(Long filmId, Long userId) {
        return List.of();
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film deleteFilm(Long id) {
        return null;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return List.of();
    }

    @Override
    public Map<Long, Set<Long>> getAllLikes() {
        return Map.of();
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Long> getLikeOfFilm(Long filmId) {
        return List.of();
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }
}
