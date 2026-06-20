package ru.yandex.practicum.filmorate.exception;

public class LikeDoesntExsistException extends RuntimeException {
  public LikeDoesntExsistException(String message) {
    super(message);
  }
}
