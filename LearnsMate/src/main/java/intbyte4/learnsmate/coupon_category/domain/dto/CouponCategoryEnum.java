package intbyte4.learnsmate.coupon_category.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CouponCategoryEnum {
    COMMON(1, "일반"),
    ANNIVERSARY(2, "기념일"),
    NEW(3, "신규가입"),
    FIRSTPURCHASE(4, "첫구매"),
    RETURN(5, "복귀"),
    COMPLETE(6, "완료"),
    EVENT(7, "이벤트");

    private final Integer categoryCode;
    private final String categoryName;

    CouponCategoryEnum(Integer categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public static Integer getCodeByName(String categoryName) {
        for(CouponCategoryEnum category : CouponCategoryEnum.values()) {
            if (category.getCategoryName().equals(categoryName)) {
                return category.getCategoryCode();
            }
        }
        throw new IllegalArgumentException("해당 카테고리의 이름이 존재하지 않습니다: " + categoryName);
    }
}
