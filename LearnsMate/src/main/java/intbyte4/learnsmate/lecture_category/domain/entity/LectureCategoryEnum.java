package intbyte4.learnsmate.lecture_category.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LectureCategoryEnum {
    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    DEVOPS("데브옵스"),
    DATABASE("데이터베이스"),
    WEB_DEVELOPMENT("웹 개발"),
    MOBILE_APP_DEVELOPMENT("모바일 앱 개발"),
    FULL_STACK("풀스택");

    private final String displayName;

    LectureCategoryEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
