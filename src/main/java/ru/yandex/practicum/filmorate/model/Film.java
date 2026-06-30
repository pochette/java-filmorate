package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validation.MinimumDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Validated
public class Film {

    @PositiveOrZero
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Size(max = 200, message = "E11 Длина описания не более 200 символов.")
    @NotBlank
    private String description;

    @MinimumDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private MPA mpa;

    private Set<Genre> genres = new HashSet<>();

}
