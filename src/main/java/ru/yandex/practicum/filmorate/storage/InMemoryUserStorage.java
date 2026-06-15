package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();

    @Override
    public User createUser(User user) {

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(users.get(id));
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userFriends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        userFriends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
        log.debug("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        userFriends.computeIfPresent(userId, (k, friends) -> {
            friends.remove(friendId);
            return friends;
        });

        userFriends.computeIfPresent(friendId, (k, friends) -> {
            friends.remove(userId);
            return friends;
        });

        log.debug("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
    }

    @Override
    public Collection<User> getFriendsByUserId(Long userId) {
        Set<Long> friendsIds = userFriends.get(userId);
        if (friendsIds == null) {
            return Collections.emptyList();
        }
        return friendsIds.stream()
                .distinct()
                .map(this::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

    }

}
