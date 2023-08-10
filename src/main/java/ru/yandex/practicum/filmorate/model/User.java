package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class User {

    @NonNull
    private long id;

    @NotBlank
    @NonNull
    private String email;

    @NotBlank
    @NonNull
    private String login;

    @Setter
    private String name;

    @NonNull
    private LocalDate birthday;
}
