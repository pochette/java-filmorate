TRUNCATE TABLE
    films_likes,
    friends,
    film_genres,
    films,
    genre_type,
    mpa_type,
    users
    RESTART IDENTITY CASCADE;

INSERT INTO mpa_type (mpa_name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17')
ON CONFLICT (mpa_name) DO NOTHING;

INSERT INTO genre_type (name)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик')
ON CONFLICT (name) DO NOTHING;