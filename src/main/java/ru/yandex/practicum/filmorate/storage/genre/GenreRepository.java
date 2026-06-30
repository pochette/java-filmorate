package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    private static final String QUERY_GET_ALL_GENRES = "SELECT * FROM GENRE_TYPE ORDER BY GENRE_ID";
    private static final String QUERY_GET_GENRE_BY_ID = "SELECT * FROM GENRE_TYPE WHERE GENRE_ID = ?";


    @Override
    public List<Genre> getAllGenres() {
        return findMany(QUERY_GET_ALL_GENRES);
    }

    @Override
    public Genre getGenreById(Integer id) {
        return findOne(QUERY_GET_GENRE_BY_ID, id);
    }
}
