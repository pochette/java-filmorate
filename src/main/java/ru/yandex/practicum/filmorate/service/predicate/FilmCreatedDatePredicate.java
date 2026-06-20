package ru.yandex.practicum.filmorate.service.predicate;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmCreatedDatePredicate implements FilmPredicate {
  @Override
  public String getErrorMessage() {
    return "Дата фильма не может быть ранее чем 28.12.1895";
  }

  @Override
  public boolean test(Film film) {

    return film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
        || film.getReleaseDate().isEqual(LocalDate.of(1895, 12, 28));
  }
}
