package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Set;


public interface LikeStorage {
    void addLike(Long userid, Long filmId);

    void deleteLike(Long userId, Long filmId);

    Set<Like> getLikesByFilm(Long filmId);

    Set<Like> getLikesByUser(Long userId);
}
