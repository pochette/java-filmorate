package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class User {
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S*", message = "Логи содержит пробелы")
    private String login;


    @NotBlank
    private String name;


    @PositiveOrZero
    private Long id;


    @Email(message = "Некорректный email")
    @NotEmpty
    private String email;


    @NotNull
    @PastOrPresent(message = "Некорректно указана дата рождения")
    private LocalDate birthday;
}

