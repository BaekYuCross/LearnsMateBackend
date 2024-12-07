package intbyte4.learnsmate.payment.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class PaymentFilterRequestVO {
    @JsonProperty("payment_code")
    private Long paymentCode;

    @JsonProperty("min_payment_price")
    private Integer minPaymentPrice;

    @JsonProperty("max_payment_price")
    private Integer maxPaymentPrice;

    @JsonProperty("start_created_at")
    private LocalDateTime startCreatedAt;

    @JsonProperty("end_created_at")
    private LocalDateTime endCreatedAt;

    private String lectureCode;
    private String lectureTitle;

    @JsonProperty("min_lecture_price")
    private Integer minLecturePrice;

    @JsonProperty("max_lecture_price")
    private Integer maxLecturePrice;

    @JsonProperty("tutor_code")
    private Long tutorCode;

    private String tutorName;
    private Long studentCode;
    private String studentName;

    @JsonProperty("lecture_category_name")
    private String lectureCategoryName;

    private String couponIssuanceCode;
    private String couponIssuanceName;
    private List<String> selectedColumns;

    @JsonSetter("lecture_code")
    public void setLectureCode(String lectureCode) {
        this.lectureCode = StringUtils.hasText(lectureCode) ? lectureCode : null;
    }

    @JsonSetter("lecture_title")
    public void setLectureTitle(String lectureTitle) {
        this.lectureTitle = StringUtils.hasText(lectureTitle) ? lectureTitle : null;
    }

    @JsonSetter("lecture_category_name")
    public void setLectureCategoryName(String categoryName) {
        this.lectureCategoryName = StringUtils.hasText(categoryName) ? categoryName : null;
    }

    @JsonSetter("tutor_name")
    public void setTutorName(String tutorName) {
        this.tutorName = StringUtils.hasText(tutorName) ? tutorName : null;
    }

    @JsonSetter("student_code")
    public void setStudentCode(Long studentCode) {
        this.studentCode = studentCode;
    }

    @JsonSetter("student_name")
    public void setStudentName(String studentName) {
        this.studentName = StringUtils.hasText(studentName) ? studentName : null;
    }

    @JsonSetter("coupon_issuance_code")
    public void setCouponIssuanceCode(String couponIssuanceCode) {
        this.couponIssuanceCode = StringUtils.hasText(couponIssuanceCode) ? couponIssuanceCode : null;
    }

    @JsonSetter("coupon_issuance_name")
    public void setCouponIssuanceName(String couponIssuanceName) {
        this.couponIssuanceName = StringUtils.hasText(couponIssuanceName) ? couponIssuanceName : null;
    }

    @JsonSetter("selected_columns")
    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }
}
