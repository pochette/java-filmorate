package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.predicate.FilmPredicate;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private static Long id = 0L;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    private final List<FilmPredicate> validators;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage, List<FilmPredicate> validators) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.validators = validators;
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));
        likeStorage.addLike(userId, filmId);
    }

    public Film createFilm(Film film) {
        //    if (film.getId() == null || filmStorage.getFilmById(film.getId()).isEmpty())
        validationFilm(film);
        return filmStorage.createFilm(film);
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

    public Collection<Like> getLikeOfFilm(Long filmId) {
        return likeStorage.getLikesByFilm(filmId);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id).orElseThrow(() -> new FilmNotFoundException("Фильм с Id = " + id + " не найден"));
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
