package ru.yandex.practicum.filmorate.service.predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserExistsPredicate implements UserPredicate {
    @Autowired
    private UserStorage userStorage;

    @Override
    public String getErrorMessage() {
        return "Пользователь не существует";
    }

    @Override
    public boolean test(User user) {
        return userStorage.getUserById(user.getId()).isEmpty();
    }
}
