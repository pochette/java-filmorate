package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
@Past
public @interface MinimumDate {
    Class<?>[] groups() default {};

    String message() default "Дата должна быть в прошлом";

    Class<?>[] payload() default {};

    String value() default "1895-12-28";

}
