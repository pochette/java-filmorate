package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    void deleteFilm(Long id);
    void deleteLike(Long filmId, Long userId);

    Optional<Film> getFilmById(Long id);

    Collection<Film> getAllFilms();

    Map<Long, Set<Long>> getAllLikes();
    Collection<Long> getLikeOfFilm(Long filmId);

    Collection<Long> addLike(Long filmId, Long userId);


}
