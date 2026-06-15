package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.warn("Validation error: {}", e.getMessage(), e);
        return new ErrorResponse("VALIDATION_ERROR", "Ошибка валидации: " + e.getMessage());
    }
    @ExceptionHandler(FriendHasAddedAlreadyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFriendHasAddedAlreadyException(final FriendHasAddedAlreadyException e){
        log.warn("Друг уже добавлен: {}", e.getMessage(), e);
        return new ErrorResponse("VALIDATION_ERROR", "Друг уже добавлен: " + e.getMessage());

    }
    @ExceptionHandler(LikeDoesntExsistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLikeDoesntExsistException(final LikeDoesntExsistException e) {
        log.warn("Лайк который удаляют не существует : {}", e.getMessage(), e);
        return new ErrorResponse("VALIDATION_ERROR", "Лайк который удаляют не существует : {}" + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        log.warn("User not found: {}", e.getMessage(), e);
        return new ErrorResponse("USER_NOT_FOUND", "Пользователь не найден: " + e.getMessage());
    }
    @ExceptionHandler(FilmDeleteFaultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmDeleteFaultException(final FilmDeleteFaultException e) {
        log.warn("Произошла ошибка при удалении фильма: {}", e.getMessage(), e);
        return new ErrorResponse("FILM_NOT_FOUND", "Фильм не найден" + e.getMessage());
    }


    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException e) {
        log.warn("Film not found: {}", e.getMessage(), e);
        return new ErrorResponse("FILM_NOT_FOUND", "Фильм не найден: " + e.getMessage());
    }


    // Обработка ошибок валидации Spring
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String field = ex.getBindingResult().getFieldErrors().getFirst().getField();
        String message = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        log.warn("Method argument validation failed for field '{}': {}", field, message, ex);
        return new ErrorResponse("FIELD_VALIDATION_FAILED", "Ошибка в поле '" + field + "': " + message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Ошибка валидации");
        log.warn("Constraint violation: {}", message, ex);
        return new ErrorResponse("CONSTRAINT_VIOLATION", message);
    }

    // Общая обработка исключений
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllUncaughtException(Exception e) {
        log.error("Unexpected server error", e);
        return new ErrorResponse("INTERNAL_ERROR", "Возникла внутренняя ошибка сервера");
    }


}
















//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleValidationException(ConstraintViolationException ex) {
//        String message = ex.getConstraintViolations().stream()
//                .map(ConstraintViolation::getMessage)
//                .findFirst()
//                .orElse("Ошибка валидации");
//
//        return new ErrorResponse("VALIDATION_ERROR", message);
//    }







