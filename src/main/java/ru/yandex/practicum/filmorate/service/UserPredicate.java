package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.function.Predicate;

public interface UserPredicate extends Predicate<User> {
    String getErrorMessage();

}
