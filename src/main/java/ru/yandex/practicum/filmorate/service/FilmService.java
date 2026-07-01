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
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final List<FilmPredicate> validators;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage, GenreStorage genreStorage, List<FilmPredicate> validators) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.validators = validators;
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));
        likeStorage.addLike(userId, filmId);
    }

    public Film createFilm(Film film) {
        validationFilm(film);
        Film createdFilm = filmStorage.createFilm(film);
        if (!film.getGenres().isEmpty()) {
            genreStorage.addGenres(film.getId(), film.getGenres().stream()
                    .map(Genre::getId)
                    .toList());
        }
        film.setGenres(film.getGenres()
                .stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return createdFilm;
    }

    private void validationFilm(Film film) {
        final var valid = validators.stream().filter(validator -> validator.test(film)).findFirst();
        valid.ifPresent(test -> {
            throw new ValidationException(test.getErrorMessage());
        });
    }

    public void deleteFilm(Long id) {
        filmStorage.getFilmById(id).orElseThrow(() -> new FilmDeleteFaultException("Произошла ошибка при удалении фильма: " + id));
        filmStorage.deleteFilm(id);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));

//        getLikeOfFilm(filmId).stream().filter(like -> like.getUserid().equals(userId)).findFirst().orElseThrow(() -> new LikeDoesntExsistException("Лайк который удаляют не существует. Id Film = " + filmId + ", id User = " + userId));
        likeStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
       return filmStorage.getFilmById(id).orElseThrow(() -> new FilmNotFoundException("Фильм с Id = " + id + " не найден"));

    }

    public Collection<Like> getLikeOfFilm(Long filmId) {
        return likeStorage.getLikesByFilm(filmId);
    }

    public Collection<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film updateFilm(Film film) {
//        validationFilm(film);
        if (filmStorage.getFilmById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException("Фильм с Id " + film.getId() + " не найден в хранилище.");
        }
        return filmStorage.updateFilm(film);
    }
}
