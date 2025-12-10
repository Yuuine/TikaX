package anthony.tikax.exception;

import anthony.tikax.entity.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Result<Object>> handleBizException(BizException e, HttpServletRequest request) {
        log.warn("business exception: URL={}, method={}, code={}",
                request.getRequestURI(),
                request.getMethod(),
                e.getCode(),
                e);

        Result<Object> result = Result.error(Integer.parseInt(e.getCode()), e.getMessage());

        return ResponseEntity.badRequest().body(result);

    }

}

