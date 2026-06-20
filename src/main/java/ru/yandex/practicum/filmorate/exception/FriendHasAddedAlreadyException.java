package ru.yandex.practicum.filmorate.exception;

public class FriendHasAddedAlreadyException extends RuntimeException {
  public FriendHasAddedAlreadyException(String message) {
    super(message);
  }
}
