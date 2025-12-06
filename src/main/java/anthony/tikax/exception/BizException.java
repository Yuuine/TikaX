package anthony.tikax.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private final String code;

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BizException(ErrorCode errorCode) {
        this(String.valueOf(errorCode.getCode()), errorCode.getMessage());
    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        this(String.valueOf(errorCode.getCode()), errorCode.getMessage(), cause);
    }
}
