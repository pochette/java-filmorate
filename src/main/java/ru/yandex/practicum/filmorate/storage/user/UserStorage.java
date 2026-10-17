package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    boolean deleteUser(Long userId);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User updateUser(User user);

}