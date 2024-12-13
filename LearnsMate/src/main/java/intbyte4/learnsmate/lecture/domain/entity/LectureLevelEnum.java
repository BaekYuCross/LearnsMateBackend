package intbyte4.learnsmate.lecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LectureLevelEnum {
    BEGINNER("BEGINNER"),
    INTERMEDIATE("INTERMEDIATE"),
    ADVANCED("ADVANCED");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LectureLevelEnum fromValue(String value) {
        for (LectureLevelEnum level : LectureLevelEnum.values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid lecture level: " + value);
    }

    public static LectureLevelEnum fromString(String value) {
        for (LectureLevelEnum level : values()) {
            if (level.getValue().equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown lecture level: " + value);
    }
}

