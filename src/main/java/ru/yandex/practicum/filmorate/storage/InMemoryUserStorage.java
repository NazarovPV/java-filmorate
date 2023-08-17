package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void add(User newUser) {
        users.put(newUser.getId(), newUser);
    }

    @Override
    public void update(User updatedUser) {
        users.put(updatedUser.getId(), updatedUser);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(long id) {
        if (!users.containsKey(id)){
            throw new NotFoundException("Задан неверный id пользователя");
        }
        return users.get(id);
    }
}