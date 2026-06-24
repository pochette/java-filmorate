package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SuppressWarnings("checkstyle:Regexp")
@Component
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public void addFriend(Long userId, Long friendId) {

    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (name, email, login, birthday) " + "values (?,?,?,?)";

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
//                .usingColumns("name", "email", "login", "birthday");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue());
        log.info("Поступил запрос на добавление пользователя. Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public User deleteUser(Long id) {
        return null;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        return List.of();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("login", user.getLogin());
        userMap.put("birthday", user.getBirthday());
        return userMap;
    }
}
