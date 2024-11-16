package intbyte4.learnsmate.coupon_category.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CouponCategoryEnum {
    COMMON("일반"),
    ANNIVERSARY("기념일"),
    NEW("신규가입"),
    FIRSTPURCHASE("첫구매"),
    RETURN("복귀"),
    COMPLETE("완료"),
    EVENT("이벤트");

    private final String displayName;

    CouponCategoryEnum(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
