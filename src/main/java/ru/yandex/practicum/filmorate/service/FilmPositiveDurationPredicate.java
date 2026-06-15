package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmPositiveDurationPredicate implements FilmPredicate{
    @Override
    public String getErrorMessage() {
        return "Продолжительность фильма должна быть положительной и больше нуля";
    }

    @Override
    public boolean test(Film film) {
        return film.getDuration().isNegative() || film.getDuration().isZero();
    }
}
