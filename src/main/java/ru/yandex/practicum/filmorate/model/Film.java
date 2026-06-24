package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class Film {

    @PositiveOrZero
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Size(max = 200, message = "E11 Длина описания не более 200 символов.")
    private String description;

    @PastOrPresent(message = "Дата должна быть в прошлом")
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    @NotBlank(message = "поле жанра не может быть пустым.")
    private Set<Genres> genres;

    private MPA mpaRate;

}
