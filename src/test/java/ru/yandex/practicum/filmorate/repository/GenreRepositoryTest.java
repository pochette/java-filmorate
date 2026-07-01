package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreRepositoryTest {
    @Autowired
    private GenreStorage genreStorage;
    @Autowired
    private MpaStorage mpaStorage;
    @Autowired
    private FilmService filmService;

    private Film film1;
    private Film film2;


    @BeforeEach
    void setUp() {
        film1 = Film.builder()
                .name("test1 Name")
                .description("Film1 description")
                .duration(100)
                .mpa(mpaStorage.getMpaById(1))
                .releaseDate(LocalDate.of(2000, 1, 1))
                .genres(Set.of(genreStorage.getGenreById(1), genreStorage.getGenreById(2)))
                .build();

        film2 = Film.builder()
                .name("test2 Name")
                .description("Film2 description")
                .duration(200)
                .mpa(mpaStorage.getMpaById(2))
                .releaseDate(LocalDate.of(1990, 10, 10))
                .genres(Set.of(genreStorage.getGenreById(3), genreStorage.getGenreById(4)))
                .build();

        filmService.createFilm(film1);
        filmService.createFilm(film2);

    }

    @Test
    void shouldReturnFilmWithGenres() {
        Set<Genre> expectedGenresFilm1 = Set.of(genreStorage.getGenreById(1), genreStorage.getGenreById(2));
        Set<Genre> expectedGenreFilm2 = Set.of((genreStorage.getGenreById(3)), genreStorage.getGenreById(4));

        Set<Genre> genresByFilm1 = filmService.getFilmById(1L).getGenres();
        Set<Genre> genresByFilm2 = filmService.getFilmById(2L).getGenres();

        System.out.println(filmService.getFilmById(1L));
        System.out.println(filmService.getFilmById(2L));

        System.out.println(expectedGenresFilm1);
        System.out.println(expectedGenreFilm2);

        Assertions.assertEquals(expectedGenresFilm1, genresByFilm1);
        Assertions.assertEquals(expectedGenreFilm2, genresByFilm2, "Жанры фильма 2 не совпадают. должны быть: " + expectedGenreFilm2);

    }

    @AfterEach
    void tearDown() {
        film1 = null;
        film2 = null;
    }

}
