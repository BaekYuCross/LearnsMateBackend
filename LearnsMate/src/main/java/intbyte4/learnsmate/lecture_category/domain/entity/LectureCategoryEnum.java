package intbyte4.learnsmate.lecture_category.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LectureCategoryEnum {
    BACKEND("BACKEND"),
    FRONTEND("FRONTEND"),
    DEVOPS("DEVOPS"),
    DATABASE("DATABASE"),
    WEB_DEVELOPMENT("WEB_DEVELOPMENT"),
    MOBILE_APP_DEVELOPMENT("MOBILE_APP_DEVELOPMENT"),
    FULL_STACK("FULL_STACK");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LectureCategoryEnum fromValue(String value) {
        for (LectureCategoryEnum category : LectureCategoryEnum.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid lecture category: " + value);
    }
}

