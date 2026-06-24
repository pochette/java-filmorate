package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void addFriend(Long userId, Long friendId);

    User createUser(User user);

    User deleteFriend(Long userId, Long friendId);

    User deleteUser(Long id);

    User deleteUser(User user);

    List<User> getAllUsers();

    List<User> getFriendsByUserId(Long userId);

    Optional<User> getUserById(Long id);

    User updateUser(User user);

}