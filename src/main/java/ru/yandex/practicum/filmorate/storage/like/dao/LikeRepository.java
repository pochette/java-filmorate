package ru.yandex.practicum.filmorate.storage.like.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.HashSet;
import java.util.Set;


@Repository
public class LikeRepository extends BaseRepository<Like> implements LikeStorage {
    private static final String INSERT_QUERY_FOR_FILM = "INSERT INTO FILMS_LIKES (USER_ID, FILM_ID) VALUES ( ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM FILMS_LIKES WHERE USER_ID = ? AND FILM_ID = ?";
    private static final String GET_LIKES_BY_FILM_QUERY = "SELECT * FROM FILMS_LIKES WHERE FILM_ID = ? ";
    private static final String GET_LIKES_BY_USER_QUERY = "SELECT * FROM FILMS_LIKES WHERE USER_ID = ?";
    private static final String QUERY_GET_ALL_LIKES = "SELECT * FROM FILMS_LIKES";


    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(Long userid, Long filmId) {
        update(INSERT_QUERY_FOR_FILM,userid, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        delete(DELETE_QUERY, userId, filmId);
    }

    @Override
    public Set<Like> getLikesByFilm(Long filmId) {
        return new HashSet<>(findMany(GET_LIKES_BY_USER_QUERY,filmId));
    }

    @Override
    public Set<Like> getLikesByUser(Long userId) {
        return new HashSet<>(findMany(GET_LIKES_BY_FILM_QUERY, userId));
    }

    @Override
    public Set<Like> getAllLikes() {
        return new HashSet<>(findMany(QUERY_GET_ALL_LIKES));
    }
}
