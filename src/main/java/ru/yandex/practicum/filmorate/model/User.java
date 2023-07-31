package ru.yandex.practicum.filmorate.model;

import lombok.*;


import javax.validation.constraints.Email;
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
