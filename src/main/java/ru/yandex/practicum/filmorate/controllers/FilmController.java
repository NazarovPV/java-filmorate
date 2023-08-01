package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@Validated
@Slf4j
public class FilmController {
    private HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;

    @GetMapping("/films")
    public ArrayList<Film> findAll() {
        return (ArrayList<Film>) new ArrayList(films.values());
    }

    @PostMapping("/films")
    public Film add(@RequestBody @Valid Film newFilm) {
        newFilm.setId(id);
        if (newFilm.getDuration() < 0) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода продолжительности фильма");
            throw new ValidationException("Введена неверная продолжительность фильма");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода года выхода");
            throw new ValidationException("Введен неверный год выпуска");
        }
        id++;
        films.put(newFilm.getId(),newFilm);
        return newFilm ;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody @Valid Film updatedFilm){
        if (!films.containsKey(updatedFilm.getId())) {
            log.info("Запрос на обновление фильма не выполнен из-за id");
            throw new ValidationException("Введен неверный id");
        }
        log.info("Запрос на обновление фильма выполнен");

        films.remove(updatedFilm.getId());
        films.put(updatedFilm.getId(),updatedFilm);
        return updatedFilm;
    }


}
