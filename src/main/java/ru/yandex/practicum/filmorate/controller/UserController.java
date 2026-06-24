package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // PUT Mapping
  @PutMapping
  public User updateUser(@RequestBody @Valid User user) {

    User updatedUser = userService.updateUser(user);
    log.debug("Обновлен пользователь: {}", updatedUser);
    return updatedUser;
  }

  @PutMapping("/{id}/friends/{friendId}")
  public void addFriend(
      @PathVariable(value = "id") @Positive Long userId, @PathVariable(value = "friendId") @Positive Long friendId) {
   userService.addFriend(userId, friendId);
    log.debug("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
  }

    // POST Mapping
  @PostMapping
  public User createUser(@RequestBody @Valid User user) {
    User createdUser = userService.createUser(user);
    log.debug("Создан пользователь: {}", createdUser);
    return createdUser;
  }

  // DELETE Mapping
  @DeleteMapping("/{id}/friends/{friendId}")
  public void deleteFriend(
          @PathVariable(value = "id") @Positive Long userId, @PathVariable @Positive Long friendId) {
    log.debug("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
    userService.removeFromFriend(userId, friendId);
  }

  //todo не работает получение списка друзей пользователя
  @GetMapping("/{id}/friends")
  public Collection<User> getListOfFriends(@PathVariable @Positive Long id) {
    log.debug("Получен список друзей пользователя {}", id);
    return userService.getFriendsByUserId(id);
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable @Positive Long id) {
    log.debug("Получен пользователь с id {}", id);
    return userService.getUserById(id);
  }

  @GetMapping
  public Collection<User> getUsers() {
    log.debug("Получен список всех пользователей");
    return userService.getAllUsers();
  }

  @GetMapping("/{id}/friends/common/{otherId}")
  public Collection<User> getCommonFriends(@PathVariable("id") @Positive Long usersId,
                                           @PathVariable @Positive Long otherId) {
    log.debug("Запрошен список общих друзей у пользователя {} с пользователем: {}", usersId, otherId);
    return userService.getCommonFriends(usersId, otherId);
  }

  @DeleteMapping("/{id}")

  public void deleteUser(@PathVariable @Positive Long id) {
    userService.deleteUser(id);

  }



}
