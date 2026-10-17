# Filmorate

> REST API фильмотеки: фильмы, лайки, друзья и рейтинг популярных фильмов.

![Java](https://img.shields.io/badge/Java_21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?logo=springboot&logoColor=white)
![Spring JDBC](https://img.shields.io/badge/Spring_JDBC-6DB33F?logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![H2](https://img.shields.io/badge/H2-tests-0000BB)
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?logo=junit5&logoColor=white)

**Русский** · [English](README.en.md)

## О проекте

Бэкенд социального сервиса о кино. Пользователи добавляют друг друга в друзья, ставят лайки фильмам и получают подборку популярных фильмов. Подборку можно отфильтровать по жанру и году выпуска.

**Ключевые решения:**

- **Чистый SQL вместо ORM.** Доступ к данным реализован на `JdbcTemplate` с собственными `RowMapper` и базовым репозиторием. Рейтинг популярности считается одним SQL-запросом с агрегацией лайков.
- **Нормализованная схема.** Семь таблиц: фильмы, пользователи, справочники жанров и рейтингов MPA, связи «фильм–жанр», дружба и лайки.
- **Собственная валидация.** Аннотация `@MinimumDate` проверяет, что дата релиза не раньше первого киносеанса (28.12.1895). Бизнес-проверки вынесены в предикаты сервисного слоя.
- **Конфигурация через окружение.** Подключение к PostgreSQL задаётся переменными окружения. Для тестов используется H2 в памяти с той же схемой.

## Стек

| Область | Технологии |
| --- | --- |
| Язык и фреймворк | Java 21, Spring Boot 3.5 (Web, JDBC, Validation, Actuator) |
| Данные | PostgreSQL, H2 (тесты), SQL-скрипты схемы и начальных данных |
| Тесты | JUnit 5, Spring Boot Test, Postman |
| Сборка | Maven, Lombok |

## Архитектура

```text
src/main/java/ru/yandex/practicum/filmorate
├── controller/   # REST: films, users, genres, mpa
├── service/      # бизнес-логика и предикаты валидации
├── storage/      # репозитории на JdbcTemplate: film, user, friend, like, genre, mpa
├── mapper/       # RowMapper для каждой сущности
├── model/        # Film, User, Genre, MPA, Like
└── validation/   # аннотация @MinimumDate
```

## Схема БД

```mermaid
erDiagram
    USERS ||--o{ FRIENDS : "дружит"
    USERS ||--o{ FILMS_LIKES : "ставит"
    FILMS ||--o{ FILMS_LIKES : "получает"
    FILMS ||--o{ FILM_GENRES : ""
    GENRE_TYPE ||--o{ FILM_GENRES : ""
    MPA_TYPE ||--o{ FILMS : ""
```

## Быстрый старт

Нужны JDK 21 и PostgreSQL. Схема и справочники создаются при запуске из `schema.sql` и `data.sql`.

```bash
export DB_URL=jdbc:postgresql://localhost:5432/filmorate
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
mvn spring-boot:run
```

API: http://localhost:8080

## API

| Ресурс | Эндпоинты |
| --- | --- |
| Фильмы | `POST /films`, `PUT /films`, `GET /films`, `GET /films/{id}`, `DELETE /films/{id}` |
| Лайки | `PUT /films/{id}/like/{userId}`, `DELETE /films/{id}/like/{userId}` |
| Популярное | `GET /films/popular?count=&genreId=&year=` |
| Пользователи | `POST /users`, `PUT /users`, `GET /users`, `GET /users/{id}`, `DELETE /users/{id}` |
| Друзья | `PUT /users/{id}/friends/{friendId}`, `DELETE /users/{id}/friends/{friendId}`, `GET /users/{id}/friends`, `GET /users/{id}/friends/common/{otherId}` |
| Справочники | `GET /genres[/{id}]`, `GET /mpa[/{id}]` |

## Тестирование

```bash
mvn test
```

- **21 тест** на JUnit 5: тесты контроллеров и валидации, а также интеграционные тесты репозиториев (`UserDaoTest`, `GenreRepositoryTest`) на H2.
- **Postman:** [коллекции](postman/) с проверками всех эндпоинтов.

## Автор

**Андрей Бурдак** — [GitHub](https://github.com/pochette) · [Telegram @burdak_aa](https://t.me/burdak_aa) · [burdak.andrew@gmail.com](mailto:burdak.andrew@gmail.com)
