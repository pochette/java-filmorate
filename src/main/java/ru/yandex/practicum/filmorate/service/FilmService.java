package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
  private static Long id = 0L;
  private final FilmStorage filmStorage;
  private final UserStorage userStorage;

  private final List<FilmPredicate> validators;

  public FilmService(
      FilmStorage filmStorage, UserStorage userStorage, List<FilmPredicate> validators) {
    this.filmStorage = filmStorage;
    this.userStorage = userStorage;
    this.validators = validators;
  }

  public Collection<Long> addLike(Long filmId, Long userId) {
    filmStorage
        .getFilmById(filmId)
        .orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
    userStorage
        .getUserById(userId)
        .orElseThrow(
            () -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));
    return filmStorage.addLike(filmId, userId);
  }

  public Film createFilm(Film film) {
    //    if (film.getId() == null || filmStorage.getFilmById(film.getId()).isEmpty())
    film.setId(getNextId());
    validationFilm(film);
    return filmStorage.createFilm(film);
  }

  //    private void validateFilm(Film film) {
  //        if (film.getName() == null || film.getName().isBlank()) {
  //            throw new ValidationException("Название фильма " + film + " не должно быть
  // пустым.");
  //        }
  //        if (film.getName().length() >= 200) {
  //            throw new ValidationException(
  //                    "Название фильма"
  //                            + film
  //                            + "слишком длинное. Максимальная длинна не более 200 символов. ");
  //        }
  //        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
  //                || film.getReleaseDate().isAfter(LocalDate.now())) {
  //            throw new ValidationException(
  //                    "Дата релиза фильма "
  //                            + film
  //                            + " не может быть раньше 28 декабря 1895 года и не может быть в
  // будущем.");
  //        }
  //        if (film.getDuration().isNegative()) {
  //            throw new ValidationException(
  //                    "Продолжительность фильма " + film + " не может быть отрицательной.");
  //        }
  //    }

  private Long getNextId() {
    return ++id;
  }

  private void validationFilm(Film film) {
    final var valid = validators.stream().filter(validator -> validator.test(film)).findFirst();
    valid.ifPresent(
        test -> {
          throw new ValidationException(test.getErrorMessage());
        });
  }

  public void deleteFilm(Long id) {
    filmStorage
        .getFilmById(id)
        .orElseThrow(
            () -> new FilmDeleteFaultException("Произошла ошибка при удалении фильма: " + id));
    filmStorage.deleteFilm(id);
  }

  public void deleteLike(Long filmId, Long userId) {
    filmStorage
        .getFilmById(filmId)
        .orElseThrow(() -> new FilmNotFoundException("Фильм с Id= " + filmId + " не найден"));
    userStorage
        .getUserById(userId)
        .orElseThrow(
            () -> new UserNotFoundException("Пользователя с ID= " + userId + " не существует"));

    getLikeOfFilm(filmId).stream()
        .filter(u -> !u.equals(userId))
        .findFirst()
        .orElseThrow(
            () ->
                new LikeDoesntExsistException(
                    "Лайк который удаляют не существует. Id Film = "
                        + filmId
                        + ", id User = "
                        + userId));

    filmStorage.deleteLike(filmId, userId);
  }

  public Collection<Long> getLikeOfFilm(Long filmId) {
    return filmStorage.getLikeOfFilm(filmId);
  }

  public Collection<Film> getAllFilms() {
    return filmStorage.getAllFilms();
  }

  public Film getFilmById(Long id) {
    return filmStorage
        .getFilmById(id)
        .orElseThrow(() -> new FilmNotFoundException("Фильм с Id = " + id + " не найден"));
  }

  public Collection<Film> getPopularFilms(Long count) {
    return filmStorage.getPopularFilms(count);
  }

  public Film updateFilm(Film film) {
    validationFilm(film);
    if (filmStorage.getFilmById(film.getId()).isEmpty()) {
      throw new FilmNotFoundException("Фильм с Id " + film.getId() + " не найден в хранилище.");
    }
    return filmStorage.updateFilm(film);
  }
}
