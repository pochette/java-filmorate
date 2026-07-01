package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String QUERY_GET_ALL_GENRES = "SELECT * FROM GENRE_TYPE ORDER BY GENRE_ID";
    private static final String QUERY_GET_GENRE_BY_ID = "SELECT * FROM GENRE_TYPE WHERE GENRE_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( ?, ? ) ";
    private static final String QUERY_FIND_GENRES_BY_FILM = "SELECT g.GENRE_ID, g.NAME FROM GENRE_TYPE AS g RIGHT JOIN PUBLIC.FILM_GENRES FG on g.GENRE_ID = FG.GENRE_ID WHERE FG.FILM_ID = ?";
    private static final String QUERY_DELETE_ALL_FROM_FILM = "DELETE FROM FILMS where FILM_ID = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addGenres(Long filmId, List<Integer> genresIds) {
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
        delete(QUERY_DELETE_ALL_FROM_FILM,filmId);

    }

    @Override
    public Set<Genre> getAllGenres() {
        return new HashSet<>(findMany(QUERY_GET_ALL_GENRES));
    }

    @Override
    public Genre getGenreById(Integer id) {
        return findOne(QUERY_GET_GENRE_BY_ID, id);
    }

    @Override
    public Set<Genre> getGenresByFilmId(Long filmId) {
        return new HashSet<>(findMany(QUERY_FIND_GENRES_BY_FILM,filmId));
    }

}
