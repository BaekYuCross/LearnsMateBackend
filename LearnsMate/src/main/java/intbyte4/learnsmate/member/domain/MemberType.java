package intbyte4.learnsmate.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MemberType {

    STUEDENT("STUDENT"),
    TUTOR("TUTOR");

    private final String userType;

    MemberType(String userType) { this.userType = userType; }

    // JSON으로 직렬화할 때 사용할 값 지정
    // Enum 직렬화될 때 getType() 메서드가 반환하는 값이 JSON으로 변환됨
    @JsonValue
    public String getType() {
        return userType;
    }
}
