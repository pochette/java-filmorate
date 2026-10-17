DELETE FROM films_likes;
DELETE FROM friends;
DELETE FROM film_genres;
DELETE FROM films;
DELETE FROM genre_type;
DELETE FROM mpa_type;
DELETE FROM users;


INSERT INTO mpa_type (mpa_name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');

INSERT INTO genre_type (name)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');
