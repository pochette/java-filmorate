# java-filmorate
Template repository for Filmorate project.


Получение всех пользователей из таблицы:
SELECT *
FROM user;

Запрос на получение общих друзей:
SELECT login
FROM (SELECT login
FROM users
WHERE user_id IN (SELECT friend_id
FROM friends
WHERE user_id = (int value)
AND status = true))
WHERE login IN (SELECT login
FROM user
WHERE user_id IN (SELECT friend_id
FROM friends
WHERE user_id = (int value)
AND status = true));

Запрос на получение списка всех фильмов:
SELECT *
FROM films;

Запрос на получение топ 10 популярных фильмов:
SELECT *
FROM film
WHERE film_id IN (SELECT film_id, count(user_id) AS likes_count
FROM likes
GROUP BY film_id
ORDER BY likes_count DESC
LIMIT (10));
