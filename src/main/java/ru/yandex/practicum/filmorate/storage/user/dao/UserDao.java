package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SuppressWarnings("checkstyle:Regexp")
@Repository

public class UserDao extends BaseRepository<User> implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO USERS (name, email, login, birthday) VALUES ( ?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ?";
    private static final String DELETE_QUERY = "DELETE FROM USERS WHERE USER_ID = ?";
    private static final String QUERY_FOR_USER_BY_ID = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String QUERY_FOR_ALL_USERS = "SELECT * FROM USERS";

    public UserDao(JdbcTemplate jdbcTemplate, UserRowMapper userMapper) {
        super(jdbcTemplate, userMapper);

    }

    @Override
    public User createUser(User user) {
        Long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday());
        user.setId(id);
        log.info("Поступил запрос на добавление пользователя. Добавлен пользователь: {}", user);
        return user;
    }


    @Override
    public boolean deleteUser(Long userId) {
        log.info("Поступил запрос на удаление пользователя с ID: {}", userId);
        return delete(DELETE_QUERY, userId);

    }

    @Override
    public List<User> getAllUsers() {
        log.info("Поступил запрос на получение списка всех пользователей");
        return findMany(QUERY_FOR_ALL_USERS);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Поступил запрос на получение пользователя с ID: {}", id);
        return Optional.ofNullable(findOne(QUERY_FOR_USER_BY_ID,id));
    }

    @Override
    public User updateUser(User user) {
        log.info("Поступил запрос на обновление пользователя с ID: {}", user.getId());
        update(UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday());
        return user;
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
