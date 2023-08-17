package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int id;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film newFilm) {
        filmValidation(newFilm);

        newFilm.setId(++id);
        filmStorage.add(newFilm);
        log.info("Запрос на добавление фильма выполнен");
        return newFilm;
    }

    public Film update(Film updatedFilm) {
        if (filmStorage.findById(updatedFilm.getId()) == null) {
            log.info("Введен неверный id");
            throw new NotFoundException("Введен неверный id");
        }
        filmValidation(updatedFilm);
        filmStorage.update(updatedFilm);
        log.info("Запрос на обновление фильма выполнен");
        return updatedFilm;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        if (filmStorage.findById(id) == null) {
            log.info("Неверно указан id");
            throw new NotFoundException("Неверно указан id");
        }
        return filmStorage.findById(id);
    }

    public void like(long id, long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        film.like(user.getId());
        log.info("Лайк поставлен");
    }

    public void unlike(long id, long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        film.unlike(user.getId());
        log.info("Лайк удален");
    }

    public List<Film> showTop(Integer count) {
        List<Film> filmSet = filmStorage.findAll();
        return filmSet.stream()
                .sorted(Comparator.comparingLong(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void filmValidation(Film film) {
        if (film.getDuration() <= 0) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода продолжительности фильма");
            throw new ValidationException("Введена неверная продолжительность фильма");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода года выхода");
            throw new ValidationException("Введен неверный год выпуска");
        }
    }

}