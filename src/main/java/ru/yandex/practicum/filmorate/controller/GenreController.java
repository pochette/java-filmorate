package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/genres")
@Validated
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Set<Genre> getAllGenres() {
        return new HashSet<>(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable @Valid @Min(1) Integer id) {
        return genreService.getGenreById(id);
    }
}





