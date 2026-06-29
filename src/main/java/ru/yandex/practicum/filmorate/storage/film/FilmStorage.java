package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    void deleteFilm(Long filmId);

    Collection<Film> filmsSearch(String title, boolean byTitle, boolean biDirector);

    List<Film> getAllFilms();

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);

    Optional<Film> getFilmById(Long filmId);

    List<Film> getFilmsByDirectorId(Integer directorId);

    Collection<Film> getPopularFilms(Long count);

    Collection<Film> getTopFilms(Integer count, Integer genreId, Integer year);

    Film updateFilm(Film film);
}
