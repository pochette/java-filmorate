package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    Collection<Long> addLike(Long filmId, Long userId);

    Film createFilm(Film film);

    Film deleteFilm(Long id);

    Film deleteLike(Long filmId, Long userId);

    Collection<Film> getAllFilms();

    Map<Long, Set<Long>> getAllLikes();

    Optional<Film> getFilmById(Long id);

    Collection<Long> getLikeOfFilm(Long filmId);

    Film updateFilm(Film film);

}
