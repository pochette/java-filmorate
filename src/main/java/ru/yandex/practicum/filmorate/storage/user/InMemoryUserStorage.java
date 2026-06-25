package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();

    @Override
    public void addFriend(Long userId, Long friendId) {
        userFriends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        userFriends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
        log.debug("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    @Override
    public User createUser(User user) {

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        User friend = users.get(friendId);

        userFriends.computeIfPresent(userId, (k, friends) -> {
            friends.remove(friendId);
            return friends;
        });

        userFriends.computeIfPresent(friendId, (k, friends) -> {
            friends.remove(userId);
            return friends;
        });

        log.debug("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
        return friend;
    }

    @Override
    public boolean deleteUser(Long userId) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    @Override
    public User deleteUser(User user) {

        users.remove(user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        Set<Long> friendsIds = userFriends.get(userId);
        if (friendsIds == null) {
            return Collections.emptyList();
        }
        return friendsIds.stream().distinct().map(this::getUserById).filter(Optional::isPresent).map(Optional::get).toList();

    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

}
