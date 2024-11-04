package intbyte4.learnsmate.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonException extends RuntimeException{
    private final StatusEnum statusEnum;
    @Override
    public String getMessage() {
        return this.statusEnum.getMessage();
    }

}