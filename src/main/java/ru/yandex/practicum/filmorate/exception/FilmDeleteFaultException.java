package ru.yandex.practicum.filmorate.exception;

public class FilmDeleteFaultException extends RuntimeException {
  public FilmDeleteFaultException(String message) {
    super(message);
  }
}
