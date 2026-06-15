package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@Validated
public class User {
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @PositiveOrZero
    private Long id;
    @Email
    private String email;
    @PastOrPresent
    private LocalDate birthday;
}

