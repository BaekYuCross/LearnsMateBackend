package intbyte4.learnsmate.lecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LectureLevelEnum {
    BEGINNER("BEGINNER"),
    INTERMEDIATE("INTERMEDIATE"),
    ADVANCED("ADVANCED");

    private final String lectureLevelType;

    LectureLevelEnum(String lectureLevelType) {
        this.lectureLevelType = lectureLevelType;
    }

    @JsonValue
    public String getLectureLevelType() {
        return lectureLevelType;
    }
}
