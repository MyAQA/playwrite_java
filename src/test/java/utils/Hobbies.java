package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Hobbies {
    SPORTS("Sports"),
    MUSIC("Music"),
    READING("Reading");

    private final String name;
}
