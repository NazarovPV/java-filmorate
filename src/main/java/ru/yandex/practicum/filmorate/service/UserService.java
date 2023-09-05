package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private int id;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User newUser) {
        userValidation(newUser);
        ArrayList<User> users = (ArrayList<User>) userStorage.findAll();
        for (User user1 : users) {
            if (user1.getEmail().equals(newUser.getEmail())) {
                log.info("Пользователь с таким email уже существует.");
                throw new ValidationException("Пользователь с таким email уже существует.");
            }
        }
        newUser.setId(++id);
        userStorage.add(newUser);
        log.info("Запрос на добавление пользователя выполнен");
        return newUser;
    }

    public User update(User updatedUser) {
        if (userStorage.findById(updatedUser.getId()) == null) {
            log.info("Запрос на обновление пользователя не выполнен. Передан неверный id");
            throw new NotFoundException("Передан неверный id");
        }
        userValidation(updatedUser);
        userStorage.update(updatedUser);
        log.info("Запрос на обновление пользователя выполнен");
        return updatedUser;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        if (userStorage.findById(id) == null) {
            log.info("Поиск по id не произведен. Передан неверный id");
            throw new NotFoundException("Передан неверный id");
        }
        return userStorage.findById(id);
    }

    public void addAsFriend(long id, long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            log.info("Добавление в друзья не произведено. Ошибка в передаче id");
            throw new NotFoundException("Ошибка в передаче id");
        }
        User user = userStorage.findById(id);
        user.addAsFriend(friendId);
        User otherUser = userStorage.findById(friendId);
        otherUser.addAsFriend(id);
        log.info("Запрос на добавление в друзья выполнен");
    }

    public void removeFriend(long id, long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            log.info("Пользователь с данным id не найден");
            throw new NotFoundException("Ошибка в передаче id");
        }
        User user = findById(id);
        User friend = findById(friendId);
        user.removeFromFriends(friendId);
        friend.removeFromFriends(id);
        userStorage.update(user);
        userStorage.update(friend);
        log.info("Запрос на удаление из друзей выполнен");
    }

    public List<User> findUserFriends(long id) {
        if (userStorage.findById(id) == null) {
            log.info("Пользователь с данным id не найден");
            throw new NotFoundException("Ошибка в передаче id");
        }
        List<User> userList = new ArrayList<>();
        Set<Long> friendsSet = userStorage.findById(id).getFriends();
        friendsSet.forEach(e -> userList.add(userStorage.findById(e)));
        return userList;
    }

    public List<User> showMutualUserFriends(long id, long otherId) {
        if (userStorage.findById(id) == null || userStorage.findById(otherId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        List<User> userMutualFriends = new ArrayList<>();
        List<Long> num = userStorage.findById(id).getFriends()
                .stream()
                .filter(userStorage.findById(otherId).getFriends()::contains)
                .collect(Collectors.toList());
        for (Long aLong : num) {
            userMutualFriends.add(userStorage.findById(aLong));
        }
        return userMutualFriends;
    }

    private void userValidation(User user) {
        if (user.getName().isEmpty()) {
            log.info("Имя присвоено по названию логина");
            user.setName(user.getLogin());
        }
        if (user.getLogin().chars().anyMatch(Character::isWhitespace)) {
            log.info("Введен логин с пробелом");
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Введен неверный год рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        String[] str = user.getEmail().split("@");
        if (str.length == 1) {
            log.info("Введена не корректная электронная почта");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

    }
}