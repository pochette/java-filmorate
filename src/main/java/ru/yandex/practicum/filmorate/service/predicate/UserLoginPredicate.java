package ru.yandex.practicum.filmorate.service.predicate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

@Service
public class UserLoginPredicate implements UserPredicate{
    @Override
    public String getErrorMessage() {
        return "Login is wrong";
    }

    @Override
    public boolean test(User user) {
        return user.getName() != null && !user.getName().isBlank();
    }
}
