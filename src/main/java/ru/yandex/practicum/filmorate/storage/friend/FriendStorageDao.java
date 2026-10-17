package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class FriendStorageDao extends BaseRepository<User> implements FriendStorage {
    private static final String QUERY_ADD_FRIEND = "INSERT INTO friends(user_id, friend_id) VALUES ( ?, ? )";
    private static final String QUERY_DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String QUERY_GET_FRIENDS_BY_USER_ID = "SELECT u.* FROM users u JOIN friends f ON u.user_id = f.friend_id WHERE f.user_id = ?";

    public FriendStorageDao(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        update(QUERY_ADD_FRIEND, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(QUERY_DELETE_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        return findMany(QUERY_GET_FRIENDS_BY_USER_ID, userId);
    }
}
