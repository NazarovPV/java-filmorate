package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;

    @NotBlank
    @NonNull
    private final String name;

    @NotBlank
    @NonNull
    @Size(max = 200)
    private final String description;

    @NonNull
    private final LocalDate releaseDate;

    @NonNull
    private final long duration;

    private final long rate;

    private Set<Long> likesId = new HashSet<>();

    public void like(long id) {
        likesId.add(id);
    }

    public void unlike(long id) {
        likesId.remove(id);
    }

    public Set<Long> getLikes() {
        return likesId;
    }

    public long getLikesCount() {
        return getLikes().size();
    }
}