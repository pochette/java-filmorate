package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class FriendStorageImpl implements FriendStorage {
    @Override
    public void addFriend(Long userId, Long friendId) {
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        return List.of();
    }
}
