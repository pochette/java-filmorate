-- data.sql
-- Добавляем жанры (только если их нет)
INSERT INTO GENRE_TYPE (GENRE_ID, NAME)
SELECT * FROM (VALUES
                   (1, 'Комедия'),
                   (2, 'Драма'),
                   (3, 'Мультфильм'),
                   (4, 'Триллер'),
                   (5, 'Документальный'),
                   (6, 'Боевик')
                  ) AS v(GENRE_ID, NAME)
WHERE NOT EXISTS (SELECT 1 FROM GENRE_TYPE WHERE GENRE_ID = v.GENRE_ID);

-- Добавляем MPA рейтинги (только если их нет)
INSERT INTO MPA_TYPE (RATING_MPA_ID, NAME)
SELECT * FROM (VALUES
                   (1, 'G'),
                   (2, 'PG'),
                   (3, 'PG-13'),
                   (4, 'R'),
                   (5, 'NC-17')
                  ) AS v(RATING_MPA_ID, NAME)
WHERE NOT EXISTS (SELECT 1 FROM MPA_TYPE WHERE RATING_MPA_ID = v.RATING_MPA_ID);