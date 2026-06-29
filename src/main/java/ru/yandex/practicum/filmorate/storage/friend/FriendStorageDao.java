package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class FriendStorageDao extends BaseRepository<User> implements FriendStorage {
    private static final String QUERY_ADD_FRIEND = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) VALUES ( ?, ? )";
    private static final String QUERY_DELETE_FRIEND = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String QUERY_GET_FRIENDS_BY_USER_ID = "SELECT u.* FROM USERS u JOIN PUBLIC.FRIENDS F ON u.USER_ID = F.FRIEND_ID WHERE F.USER_ID = ?";

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
