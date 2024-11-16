package intbyte4.learnsmate.lecture_category.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LectureCategoryEnum {
    BACKEND("BACKEND"),
    FRONTEND("FRONTEND"),
    DEVOPS("DEVOPS"),
    DATABASE("DATABASE"),
    WEB_DEVELOPMENT("WEB_DEVELOPMENT"),
    MOBILE_APP_DEVELOPMENT("MOBILE_APP_DEVELOPMENT"),
    FULL_STACK("FULL_STACK");

    private final String displayName;

    LectureCategoryEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
