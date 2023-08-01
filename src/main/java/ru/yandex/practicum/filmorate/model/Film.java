package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    @NonNull
    @Setter
    private long id;

    @NotBlank
    @NonNull
    private String name;

    @NotBlank
    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    private long duration;

    @NonNull
    private LocalDate releaseDate;
}
