package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.FriendHasAddedAlreadyException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.predicate.UserPredicate;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Слой Отвечает за Не должен делать Controller HTTP-запросы и ответы Работать с Map, SQL Service
 * Бизнес-правила Знать детали хранения Storage Сохранение и получение данных Проверять
 * бизнес-логикуggt Model Данные (User, Film) Выполнять операции
 */
@Service
@Validated
@Slf4j
public class UserService {
    private static Long id = 0L;
    private final UserStorage userStorage;
    private final List<UserPredicate> validators;

    public UserService(UserStorage userStorage, List<UserPredicate> validators) {
        this.userStorage = userStorage;
        this.validators = validators;
    }

    public void addFriend(Long userId, Long friendId) {
        if (getUserById(userId) == null || getUserById(friendId) == null) {
            throw new UserNotFoundException("При добавлении в друзья произошла ошибка. Один из пользователей не найден. id = " + userId + ", friendId = " + friendId);
        }
        Collection<User> usersFriend = getFriendsByUserId(userId);

        usersFriend.stream().filter((user) -> user.getId().equals(friendId)).findFirst().ifPresent(user -> {
            throw new FriendHasAddedAlreadyException("Друг с ID: " + friendId + " уже добавлен к пользователю с ID: " + userId);
        });

        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
        log.debug("Пользователь {} добавил в друзья {}", userId, friendId);
    }

    public User createUser(User user) {
        if (user.getId() == null || userStorage.getUserById(user.getId()).isEmpty()) user.setId(getNextId());
        final var valid = validators.stream().filter(validator -> !validator.test(user)).findFirst();
        valid.ifPresent(validator -> {
            throw new ValidationException(validator.getErrorMessage());
        });


        return userStorage.createUser(user);
    }

    private Long getNextId() {
        return ++id;
    }

    //TODO Не работает удаление пользователея, 500 ошибка
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
        if (usersFriends.isEmpty() || otherUserFriends.isEmpty() ) {
            return Collections.emptySet();
        }


        return usersFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());

    }


//        return users.stream().skip(1).map(user -> new HashSet<>(getFriendsByUserId(user.getId()))).filter(commonFriends -> {
//            if (commonFriends.isEmpty()) {
//                throw new UserNotFoundException("У пользователя нет друзей -  общих друзей быть не может");
//            }
//            return true;
//        }).allMatch(currentFriends -> {
//            firstUsersFriends.retainAll(currentFriends);
//            return !firstUsersFriends.isEmpty();
//        }) ? new ArrayList<>(firstUsersFriends) : Collections.emptyList();
//    }


//    Iterator<Long> iter = userIds.iterator();
//    Set<User> commonFriends = new HashSet<>(getFriendsByUserId(iter.next()));
//    while (iter.hasNext()) {
//      Set<User> current = new HashSet<>(getFriendsByUserId(iter.next()));
//
//      commonFriends.retainAll(current);
//      if (commonFriends.isEmpty()) {
//        return Collections.emptyList();
//      }
//    }
//    return new ArrayList<>(commonFriends);


    public Collection<User> getFriendsByUserId(Long userId) {
        if (getUserById(userId) == null) {
            return Collections.emptySet();
        }
        return userStorage.getFriendsByUserId(userId);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден ID= " + id));
    }

    public void removeFromFriend(Long userId, Long friendId) {

        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь " + userId + " не найден"));

        userStorage.getUserById(friendId).orElseThrow(() -> new UserNotFoundException("Пользователь " + friendId + " не найден"));

        boolean isFriend = userStorage.getFriendsByUserId(userId).stream().anyMatch(friend -> friend.getId().equals(friendId));

        if (!isFriend) {
            throw new ValidationException("Пользователь " + friendId + " не находится в друзьях у пользователя " + userId);
        }
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId()).orElseThrow(() -> new UserNotFoundException("Пользователь под номером = " + user.getId() + " не найден."));
        return userStorage.updateUser(user);
    }
}
