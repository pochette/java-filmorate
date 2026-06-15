package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Builder
@Validated
public class User {
  @PositiveOrZero private Long id;
  @Email private String email;
  @NotBlank private String login;
  @NotBlank private String name;
  @PastOrPresent private LocalDate birthday;

  public User(Long id, String email, String login, String name, LocalDate birthday) {
    this.id = id;
    this.email = email;
    this.login = login;
    this.name = name;
    this.birthday = birthday;
  }

  public User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }
}
