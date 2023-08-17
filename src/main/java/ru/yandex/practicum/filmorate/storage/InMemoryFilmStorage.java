package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public void add(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
    }

    @Override
    public void update(Film updatedFilm) {
        films.put(updatedFilm.getId(), updatedFilm);
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList(films.values());
    }

    @Override
    public Film findById(long id) {
        if (!films.containsKey(id)){
            throw new NotFoundException("Задан неверный id фильма");
        }

        return films.get(id);
    }
}