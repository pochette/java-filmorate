package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

  private final Map<Long, Film> films = new HashMap<>();
  private final Map<Long, Set<Long>> likes = new HashMap<>();

  @Override
  public Film createFilm(Film film) {
    films.put(film.getId(), film);
    return films.get(film.getId());
  }

  @Override
  public Film updateFilm(Film film) {
    films.replace(film.getId(), film);
    return film;
  }

  @Override
  public void deleteFilm(Long id) {
    films.remove(id);
    likes.remove(id);
  }

  @Override
  public void deleteLike(Long filmId, Long userId) {
    likes.computeIfPresent(filmId, (id, users) -> {
      users.remove(userId);
      return users;
    });
  }

  @Override
  public Optional<Film> getFilmById(Long id) {
    return Optional.ofNullable(films.get(id));
  }

  @Override
  public Collection<Film> getAllFilms() {
    return films.values().stream().toList();
  }

  @Override
  public Map<Long, Set<Long>> getAllLikes() {
    return new HashMap<>(likes);
  }

  @Override
  public Collection<Long> getLikeOfFilm(Long filmId) {
    return likes.getOrDefault(filmId, Collections.emptySet());
  }

  @Override
  public Collection<Long> addLike(Long filmId, Long userId) {

    likes.computeIfAbsent(filmId, value -> new HashSet<>()).add(userId);
    return likes.get(filmId);
  }
}
