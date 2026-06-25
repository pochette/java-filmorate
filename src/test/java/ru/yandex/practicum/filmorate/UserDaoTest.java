package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDao;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDao.class, UserMapper.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserDaoTest {
    private static User user1;
    private static User user2;
    private static User user3;
    private final UserDao userDao;

    @Test
    void createUserTest() {
        User returnUser = userDao.createUser(user1);
        assertThat(user1).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(returnUser).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(returnUser).hasFieldOrPropertyWithValue("login", "user1Login");
        assertThat(returnUser).hasFieldOrPropertyWithValue("email", "burdak1994@gmail.com");

        User returnUser2 = userDao.createUser(user2);
        assertThat(user2).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(returnUser2).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(returnUser2).hasFieldOrPropertyWithValue("login", "user2login");
        assertThat(returnUser2).hasFieldOrPropertyWithValue("email", "burda24@mail.ru");

        User returnUser3 = userDao.createUser(user3);
        assertThat(user3).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(returnUser3).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(returnUser3).hasFieldOrPropertyWithValue("login", "user3Login");
        assertThat(returnUser3).hasFieldOrPropertyWithValue("email", "x.burdak@mail.ru");

    }

    @Test
    void deleteUserNotFoundTest() {
        userDao.createUser(user1);
        userDao.createUser(user2);
        userDao.createUser(user3);

        assertThat(userDao.getAllUsers()).hasSize(3);

        boolean isDeleted = userDao.deleteUser(4L);

        // Ожидаем, что ничего не было удалено
        assertThat(isDeleted).isFalse();

        // Проверяем, что количество пользователей не изменилось
        assertThat(userDao.getAllUsers()).hasSize(3);
    }

    @Test
    void deleteUserTest() {
        userDao.createUser(user1);
        userDao.createUser(user2);
        userDao.createUser(user3);

        assertThat(userDao.getAllUsers()).hasSize(3);

        userDao.deleteUser(2L);
        assertThat(userDao.getAllUsers()).hasSize(2);
    }

    @Test
    void getAllUsersTest() {
        userDao.createUser(user1);
        userDao.createUser(user2);
        userDao.createUser(user3);

        assertThat(userDao.getAllUsers()).hasSize(3);
    }

    @Test
    void getUserByIdTest() {
        userDao.createUser(user1);
        User returnUser = userDao.getUserById(1L).orElseThrow();
        assertThat(returnUser).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(returnUser).hasFieldOrPropertyWithValue("login", "user1Login");
        assertThat(returnUser).hasFieldOrPropertyWithValue("email", "burdak1994@gmail.com");
    }

    @BeforeEach
    void init() {
        user1 = User.builder()
                .login("user1Login")
                .name("user1Name")
                .birthday(LocalDate.of(1994, 1, 29))
                .email("burdak1994@gmail.com")
                .build();

        user2 = User.builder()
                .login("user2login")
                .name("user2Login")
                .email("burda24@mail.ru")
                .birthday(LocalDate.of(1995, 4, 18))
                .build();

        user3 = User.builder()
                .login("user3Login")
                .name("user3Name")
                .email("x.burdak@mail.ru")
                .birthday(LocalDate.of(1990, 12, 6))
                .build();

    }

    @AfterEach
    void tearDown() {
        List<User> users = userDao.getAllUsers();
        if (!users.isEmpty()) {
            for (User user : users) {
                userDao.deleteUser(user.getId());
            }
        }

    }

    @Test
    void updateUserTest() {
        userDao.createUser(user1);
        User updatedUser = User.builder()
                .id(1L)
                .login("updatedLogin")
                .name("updatedName")
                .email("updatedEmail@gmail.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userDao.updateUser(updatedUser);
        User returnUser = userDao.getUserById(1L).orElseThrow(() -> new UserNotFoundException("Пользователь не найден "));
        assertThat(returnUser).hasFieldOrPropertyWithValue("login", "updatedLogin");
        assertThat(returnUser).hasFieldOrPropertyWithValue("name", "updatedName");
        assertThat(returnUser).hasFieldOrPropertyWithValue("email", "updatedEmail@gmail.com");
    }

}
