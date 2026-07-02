package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDeleteFaultException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.predicate.FilmPredicate;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final List<FilmPredicate> validators;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage, GenreStorage genreStorage, MpaStorage mpaStorage, List<FilmPredicate> validators) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.validators = validators;
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));
        likeStorage.addLike(userId, filmId);
    }

    public Film createFilm(Film film) {
        validationFilm(film);
        filmStorage.createFilm(film);
        saveGenres(film);
        return film;
    }

    private void validationFilm(Film film) {
        final var valid = validators.stream().filter(validator -> validator.test(film)).findFirst();
        valid.ifPresent(test -> {
            throw new ValidationException(test.getErrorMessage());
        });
    }

    private void saveGenres(Film film) {
        if (!film.getGenres().isEmpty()) {
            genreStorage.deleteGenres(film.getId());
            genreStorage.addGenres(film.getId(), film.getGenres().stream()
                    .map(Genre::getId)
                    .toList());
        } else {
            genreStorage.deleteGenres(film.getId());
        }
    }

    public void deleteFilm(Long id) {
        filmStorage.getFilmById(id).orElseThrow(() -> new FilmDeleteFaultException("Произошла ошибка при удалении фильма: " + id));
        filmStorage.deleteFilm(id);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));
        likeStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        Map<Long, Set<Genre>> genres =
                genreStorage.getGenresByFilmIds(
                        films.stream()
                                .map(Film::getId)
                                .toList());
        for (Film film : films) {
            film.setGenres(
                    genres.getOrDefault(film.getId(), new LinkedHashSet<>()));
            film.setMpa(
                    mpaStorage.getMpaById(film.getMpa().getId())
            );
        }

        return films;
    }

    public Collection<Like> getLikeOfFilm(Long filmId) {
        return likeStorage.getLikesByFilm(filmId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        Collection<Film> popularFilms = filmStorage.getPopularFilms(count);
        popularFilms.forEach(film -> {
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        });
        return popularFilms;
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        saveGenres(film);
        return getFilmById(film.getId());
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id).orElseThrow(() -> new FilmNotFoundException("Фильм с Id = " + id + " не найден"));
        loadGenres(film);
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        return film;
    }

    private void loadGenres(Film film) {
        film.setGenres(
                genreStorage.getGenresByFilmId(film.getId())
        );
    }
}
