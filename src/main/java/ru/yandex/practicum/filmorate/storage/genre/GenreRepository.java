package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String QUERY_GET_ALL_GENRES = "SELECT * FROM GENRE_TYPE ORDER BY GENRE_ID";
    private static final String QUERY_GET_GENRE_BY_ID = "SELECT * FROM GENRE_TYPE WHERE GENRE_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( ?, ? ) ";
    private static final String QUERY_FIND_GENRES_BY_FILMS = "" +
            "SELECT fg.FILM_ID, g.GENRE_ID, g.NAME " +
            "FROM FILM_GENRES fg " +
            "JOIN GENRE_TYPE g ON fg.GENRE_ID = g.GENRE_ID " +
            "WHERE fg.FILM_ID IN (%s) " +
            "ORDER BY fg.FILM_ID, g.GENRE_ID";
    private static final String QUERY_FIND_GENRES_BY_FILM = "" +
            "SELECT fg.FILM_ID, g.GENRE_ID, g.NAME " +
            "FROM FILM_GENRES fg " +
            "JOIN GENRE_TYPE g ON fg.GENRE_ID = g.GENRE_ID " +
            "WHERE fg.FILM_ID = ? " +
            "ORDER BY fg.FILM_ID, g.GENRE_ID" +
            "";
    private static final String QUERY_DELETE_ALL_FROM_FILM = "DELETE FROM FILM_GENRES where FILM_ID = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addGenres(Long filmId, List<Integer> genresIds) {
        log.debug("Добавление жанров для фильма {}: {}", filmId, genresIds); // ← добавьте лог

        batchUpdateBase(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setInt(2, genresIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return genresIds.size();
            }
        });
    }

    @Override
    public void deleteGenres(Long filmId) {
        delete(QUERY_DELETE_ALL_FROM_FILM, filmId);

    }

    @Override
    public Set<Genre> getAllGenres() {
        return new LinkedHashSet<>(findMany(QUERY_GET_ALL_GENRES));
    }

    @Override
    public Genre getGenreById(Integer id) {
        return findOne(QUERY_GET_GENRE_BY_ID, id);
    }

    @Override
    public Set<Genre> getGenresByFilmId(Long filmId) {
        return new LinkedHashSet<>(findMany(QUERY_FIND_GENRES_BY_FILM, filmId));
    }

    @Override
    public Map<Long, Set<Genre>> getGenresByFilmIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = QUERY_FIND_GENRES_BY_FILMS.formatted(placeholders(ids.size()));

        return query(sql, rs -> {
            Map<Long, Set<Genre>> genres = new HashMap<>();
            while (rs.next()) {
                Long filmId = rs.getLong("FILM_ID");
                genres.computeIfAbsent(filmId,
                                id -> new LinkedHashSet<>())
                        .add(mapper.mapRow(rs, rs.getRow()));
            }
            return genres;
        }, ids.toArray());

    }
}
