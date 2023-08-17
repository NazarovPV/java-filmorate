package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    public interface Update{

    }
    @NotNull(groups = {Update.class})
    private long id;

    @NotBlank
    @NonNull
    private String name;

    @NotBlank
    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    private long duration;

    private long rate;

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