package ru.yandex.practicum.filmorate.exception;

public class FilmNotUpdatedException extends RuntimeException {
  public FilmNotUpdatedException(String message) {
    super(message);
  }
}
