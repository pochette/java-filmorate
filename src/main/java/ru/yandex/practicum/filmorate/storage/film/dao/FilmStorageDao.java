package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmDeleteFaultException;
import ru.yandex.practicum.filmorate.exception.FilmNotUpdatedException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmStorageDao extends BaseRepository<Film> implements FilmStorage {
    private static final String INSERT_QUERY = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM FILMS WHERE FILM_ID = ? ";
    private static final String QUERY_GET_POPULAR_FILMS = "SELECT * FROM FILMS f JOIN PUBLIC.MPA_TYPE m on f.MPA_ID = m.MPA_ID LEFT JOIN (SELECT FILM_ID, COUNT(FILM_ID) AS LIKES FROM FILMS_LIKES GROUP BY FILM_ID) fl " +
            "ON f.FILM_ID = fl.FILM_ID ORDER BY LIKES DESC LIMIT ?";
    private static final String QUERY_FOR_ALL_FILMS = "SELECT * FROM FILMS f, MPA_TYPE m WHERE f.MPA_ID = m.MPA_ID";
    private static final String QUERY_FOR_FILM_BY_ID = "SELECT * FROM FILMS f, MPA_TYPE m WHERE m.MPA_ID = f.MPA_ID AND f.FILM_ID = ? ";
    private static final String QUERY_FOR_UPDATE_FILM = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";

    public FilmStorageDao(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper) {
        super(jdbcTemplate, filmRowMapper);
    }

    @Override
    public Film createFilm(Film film) {
        Long id = insert(INSERT_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        film.setId(id);
        log.info("Поступил запрос на добавление фильма. Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public void deleteFilm(Long id) {

        Optional<Film> film = getFilmById(id);
        if (film.isPresent()) {
            boolean isDeleted = delete(DELETE_QUERY, id);
            if (!isDeleted) {
                throw new FilmDeleteFaultException("Произошла ошибка при удалении фильма с ID: " + id);
            }
        }

    }

    @Override
    public Collection<Film> filmsSearch(String title, boolean byTitle, boolean biDirector) {
        return List.of();
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(QUERY_FOR_ALL_FILMS);
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return List.of();
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.ofNullable(findOne(QUERY_FOR_FILM_BY_ID, id));
    }

    @Override
    public List<Film> getFilmsByDirectorId(Integer directorId) {
        return List.of();
    }

    @Override
    public Collection<Film> getPopularFilms(Long count) {
        return findMany(QUERY_GET_POPULAR_FILMS,count);
    }

    @Override
    public Collection<Film> getTopFilms(Integer count, Integer genreId, Integer year) {
        return List.of();
    }

    @Override
    public Film updateFilm(Film film) {

        boolean isUpdated = update(QUERY_FOR_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (isUpdated) {
            return film;
        } else {
            throw new FilmNotUpdatedException("Произошла ошибка при обновлении фильма: " + film);
        }
    }
}
