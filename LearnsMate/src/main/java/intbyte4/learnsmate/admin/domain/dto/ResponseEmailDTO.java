package intbyte4.learnsmate.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.ExceptionDTO;
import intbyte4.learnsmate.common.exception.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseEmailDTO<T> {

    @JsonIgnore
    private HttpStatus httpStatus;

    @NotNull
    private boolean success;

    @Nullable
    private T data;

    @Nullable
    private ExceptionDTO error;

    // static 팩토리 메소드
    public static <T> ResponseEmailDTO<T> ok(T data) {
        return new ResponseEmailDTO<>(
                HttpStatus.OK,
                true,
                data,
                null
        );
    }

    public static ResponseEmailDTO<Object> fail(@NotNull CommonException e) {
        return new ResponseEmailDTO<>(
                e.getStatusEnum().getHttpStatus(),
                false,
                null,
                ExceptionDTO.of(e.getStatusEnum())
        );
    }

    public static ResponseEmailDTO<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseEmailDTO<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                ExceptionDTO.of(StatusEnum.MISSING_REQUEST_PARAMETER)
        );
    }

    public static ResponseEmailDTO<Object> fail(final MethodArgumentTypeMismatchException e) {
        return new ResponseEmailDTO<>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                false,
                null,
                ExceptionDTO.of(StatusEnum.INVALID_PARAMETER_FORMAT)
        );
    }
}
