//package ru.yandex.practicum.filmorate.storage.film;
//
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.*;
//
//public class InMemoryFilmStorage implements FilmStorage {
//
//    private final Map<Long, Film> films = new HashMap<>();
//    private final Map<Long, Set<Long>> likes = new HashMap<>();
//
//    @Override
//    public void addLike(Long filmId, Long userId) {
//
//        likes.computeIfAbsent(filmId, value -> new HashSet<>()).add(userId);
//        likes.get(filmId);
//    }
//
//    @Override
//    public Film createFilm(Film film) {
//        films.put(film.getId(), film);
//        return films.get(film.getId());
//    }
//
//    @Override
//    public List<Film> getAllFilms() {
//        return films.values().stream().toList();
//    }
//
//    @Override
//    public Film updateFilm(Film film) {
//        films.replace(film.getId(), film);
//        return film;
//    }
//
//    @Override
//    public void deleteFilm(Long id) {
//        films.remove(id);
//        likes.remove(id);
//    }
//
//    @Override
//    public Optional<Film> getFilmById(Long id) {
//        return Optional.ofNullable(films.get(id));
//    }
//
//    @Override
//    public Film deleteLike(Long filmId, Long userId) {
//        likes.computeIfPresent(filmId, (id, users) -> {
//            users.remove(userId);
//            return users;
//        });
//        return null;
//    }
//
//    @Override
//    public Map<Long, Set<Long>> getAllLikes() {
//        return new HashMap<>(likes);
//    }
//
//    @Override
//    public Collection<Long> getLikeOfFilm(Long filmId) {
//        return likes.getOrDefault(filmId, Collections.emptySet());
//    }
//}
