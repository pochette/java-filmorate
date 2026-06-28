package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@Validated
@RequestMapping("films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") @Positive Long idFilm, @PathVariable(value = "userId") @Positive Long idUser) {
        filmService.addLike(idFilm, idUser);
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        Film createdFilm = filmService.createFilm(film);
        log.debug("Добавлен фильм: {}", createdFilm);
        return createdFilm;
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable @Positive Long id) {
        filmService.deleteFilm(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") @Positive Long filmId, @PathVariable(value = "userId") @Positive Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @Positive Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(value = "count", required = false, defaultValue = "10") @Min(0) Long count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        log.debug("Обновлен фильм: {}", updatedFilm);

        return updatedFilm;
    }

}
