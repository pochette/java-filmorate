package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.FriendHasAddedAlreadyException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.predicate.UserPredicate;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Слой Отвечает за Не должен делать Controller HTTP-запросы и ответы Работать с Map, SQL Service
 * Бизнес-правила Знать детали хранения Storage Сохранение и получение данных Проверять
 * бизнес-логикуggt Model Данные (User, Film) Выполнять операции
 */

@Validated
@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final List<UserPredicate> validators;
    private final FriendStorage friendStorage;

    public UserService(UserStorage userStorage, List<UserPredicate> validators, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.validators = validators;
        this.friendStorage = friendStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (getUserById(userId) == null || getUserById(friendId) == null) {
            throw new UserNotFoundException("При добавлении в друзья произошла ошибка. Один из пользователей не найден. id = " + userId + ", friendId = " + friendId);
        }
        Collection<User> usersFriend = getFriendsByUserId(userId);

        usersFriend.stream().filter((user) -> user.getId().equals(friendId)).findFirst().ifPresent(user -> {
            throw new FriendHasAddedAlreadyException("Друг с ID: " + friendId + " уже добавлен к пользователю с ID: " + userId);
        });

        friendStorage.addFriend(userId, friendId);
//        friendStorage.addFriend(friendId, userId);
        log.debug("Пользователь {} добавил в друзья {}", userId, friendId);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден ID= " + id));
    }

    public Collection<User> getFriendsByUserId(Long userId) {
        if (getUserById(userId) == null) {
            return Collections.emptySet();
        }
        return friendStorage.getFriendsByUserId(userId);
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public void deleteUser(Long id) {
        userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException("Пользователь для удаления не найден ID= " + id));
        userStorage.deleteUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Collection<User> getCommonFriends(Long userId,
                                             Long otherId) {

        Set<User> usersFriends = new HashSet<>(getFriendsByUserId(userId));
        Set<User> otherUserFriends = new HashSet<>(getFriendsByUserId(otherId));
        if (usersFriends.isEmpty() || otherUserFriends.isEmpty()) {
            return Collections.emptySet();
        }

        return usersFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());

    }

    public void removeFromFriend(Long userId, Long friendId) {

        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь " + userId + " не найден"));

        userStorage.getUserById(friendId).orElseThrow(() -> new UserNotFoundException("Пользователь " + friendId + " не найден"));

        boolean isFriend = friendStorage.getFriendsByUserId(userId).stream().anyMatch(friend -> friend.getId().equals(friendId));

        if (!isFriend) {
            throw new ValidationException("Пользователь " + friendId + " не находится в друзьях у пользователя " + userId);
        }
        friendStorage.deleteFriend(userId, friendId);
        friendStorage.deleteFriend(friendId, userId);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId()).orElseThrow(() -> new UserNotFoundException("Пользователь под номером = " + user.getId() + " не найден."));
        return userStorage.updateUser(user);
    }
}
