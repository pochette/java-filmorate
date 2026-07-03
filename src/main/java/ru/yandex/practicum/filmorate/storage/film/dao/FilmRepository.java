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
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    //language=H2
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    //language=H2
    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ? ";
    private static final String QUERY_GET_POPULAR_FILMS_TO_LIMIT = "SELECT f.*, m.mpa_name, likes  FROM films f JOIN mpa_type m ON f.mpa_id = m.mpa_id LEFT JOIN (SELECT film_id, COUNT(film_id) AS likes FROM films_likes GROUP BY film_id) fl" + " ON f.film_id = fl.film_id ORDER BY likes LIMIT ?";
    private static final String QUERY_FOR_ALL_FILMS = "SELECT " + "f.*," + "m.mpa_name " + "FROM films f " + "JOIN mpa_type m ON f.mpa_id = m.mpa_id";
    private static final String QUERY_FOR_FILM_BY_ID = "SELECT f.*, m.mpa_name FROM films f JOIN mpa_type m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";
    private static final String QUERY_FOR_UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    //language=H2
    private static final String QUERY_GET_GENRES_BY_FILM_ID = "SELECT * FROM film_genres fg, genre_type gt WHERE fg.genre_id = gt.genre_id AND film_id = ?";
    //language=H2
    private static final String QUERY_GET_POPULAR_FILMS_BY_GENRE_AND_YEAR = "SELECT\n" +
            "        f.*,\n" +
            "        m.mpa_name,\n" +
            "        COALESCE(fl.likes, 0) AS likes\n" +
            "    FROM films f\n" +
            "    JOIN mpa_type m\n" +
            "        ON m.mpa_id = f.mpa_id\n" +
            "    LEFT JOIN (\n" +
            "        SELECT\n" +
            "            film_id,\n" +
            "            COUNT(*) AS likes\n" +
            "        FROM films_likes\n" +
            "        GROUP BY film_id\n" +
            "    ) fl\n" +
            "        ON fl.film_id = f.film_id\n" +
            "    WHERE (\n" +
            "        ? IS NULL\n" +
            "        OR EXISTS (\n" +
            "            SELECT 1\n" +
            "            FROM film_genres fg\n" +
            "            WHERE fg.film_id = f.film_id\n" +
            "              AND fg.genre_id = ?\n" +
            "        )\n" +
            "    )\n" +
            "    AND (\n" +
            "        ? IS NULL\n" +
            "        OR EXTRACT(YEAR FROM f.release_date) = ?\n" +
            "    )\n" +
            "    ORDER BY COALESCE(fl.likes, 0) DESC\n" +
            "    LIMIT ?";

    public FilmRepository(JdbcTemplate jdbcTemplate, FilmRowMapper filmRowMapper) {
        super(jdbcTemplate, filmRowMapper);
    }

    @Override
    public Film createFilm(Film film) {
        Long id = insert(INSERT_QUERY, "film_id", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
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
    public Collection<Film> getPopularFilms(Integer count, Integer year, Integer genreId) {
        return findMany(QUERY_GET_POPULAR_FILMS_BY_GENRE_AND_YEAR, genreId, genreId, year, year, count);
    }

    @Override
    public Collection<Film> getTopFilms(Integer count, Integer genreId, Integer year) {
        return List.of();
    }

    @Override
    public Film updateFilm(Film film) {

        boolean isUpdated = update(QUERY_FOR_UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (isUpdated) {
            return film;
        } else {
            throw new FilmNotUpdatedException("Произошла ошибка при обновлении фильма: " + film);
        }
    }
}