package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@Validated
@Slf4j
public class UserController {
    private HashMap<Long, User> users = new HashMap<>();
    private long id=1;

    @GetMapping("/users")
    public ArrayList<User> findAll() {
        return (ArrayList<User>) new ArrayList(users.values());
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        user.setId(id);
        if (user.getName() == null) {
            log.info("Имя присвоено по названию логина");
            user.setName(user.getLogin());
        }
        if (user.getLogin().chars().anyMatch(Character::isWhitespace)) {
            log.info("Введен логин с пробелом");
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Введен неверный год рождения");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        String[] str = user.getEmail().split("@");
        if (str.length == 1) {
            log.info("Введена не корректная электронная почта");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User updatedUser) {
        if (!users.containsKey(updatedUser.getId())){
            log.info("Запрос на обновление пользователя не выполнен, не корректный id");
            throw new ValidationException("Передан неверный id");
        }
        log.info("Запрос на обновление пользователя выполнен");
        users.remove(updatedUser.getId());
        users.put(updatedUser.getId(), updatedUser);
        return updatedUser;
    }

}
