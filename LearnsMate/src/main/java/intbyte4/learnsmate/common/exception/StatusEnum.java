package intbyte4.learnsmate.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum StatusEnum {

    WRONG_ENTRY_POINT(400, HttpStatus.BAD_REQUEST, "잘못된 접근입니다"),
    MISSING_REQUEST_PARAMETER(400, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    DATA_INTEGRITY_VIOLATION(400, HttpStatus.BAD_REQUEST, "데이터 무결성 위반입니다. 필수 값이 누락되었거나 유효하지 않습니다."),
    INVALID_PARAMETER_FORMAT(400, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    INVALID_PASSWORD(400, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    RESTRICTED(403, HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    DELETE_NOT_ALLOWED(403, HttpStatus.FORBIDDEN, "삭제할 수 없습니다."),
    UPDATE_NOT_ALLOWED(403, HttpStatus.FORBIDDEN, "수정할 수 없습니다."),


    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    STUDENT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 학생입니다."),
    ADMIN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 직원입니다."),
    LECTURE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 강의입니다."),
    VIDEO_BY_LECTURE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 강의별 동영상입니다."),
    LECTURE_CATEGORY_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 강의카테고리 입니다."),
    EMAIL_NOT_FOUND(404, HttpStatus.NOT_FOUND, "회원가입 되지 않은 아이디입니다."),
    TEMPLATE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 템플릿입니다."),
    CAMPAIGN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 캠페인입니다."),
    PAYMENT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 결제내역입니다."),
    CONTRACT_PROCESS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 계약과정입니다."),
    VOC_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 VOC 입니다."),
    COUPON_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COMMENT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    LOGIN_HISTORY_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 로그인 내역입니다."),
    PREFERRED_TOPICS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 선호 주제입니다."),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다"),

    INVALID_VERIFICATION_CODE(400, HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    EMAIL_VERIFICATION_REQUIRED(400, HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다."),

    EMAIL_DUPLICATE(409, HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    NICKNAME_DUPLICATE(409, HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    COUPON_ALREADY_USED(409, HttpStatus.CONFLICT, "사용된 쿠폰입니다.");

    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String message;

    StatusEnum(int statusCode, HttpStatus httpStatus, String message) {
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

