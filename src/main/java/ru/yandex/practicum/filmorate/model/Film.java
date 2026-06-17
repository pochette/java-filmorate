package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

/**
 * Film.
 */


@Data
@Validated
public class Film {
    @PositiveOrZero
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Size(max = 200, message = "E11 Длина описания не более 200 символов.")
    private String description;

    @Past(message = "Дата должна быть в прошлом")
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

}
