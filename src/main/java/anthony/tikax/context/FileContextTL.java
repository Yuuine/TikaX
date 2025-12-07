package anthony.tikax.context;

import anthony.tikax.domain.model.FileProcessingContext;
import org.springframework.stereotype.Component;

/**
 * 文件处理上下文线程本地变量
 */
@Component
public class FileContextTL {

    private static final ThreadLocal<FileProcessingContext> CTX = new ThreadLocal<>();

    public static void set(FileProcessingContext context) {
        CTX.set(context);
    }

    public static FileProcessingContext get() {
        return CTX.get();
    }

    public static void clear() {
        CTX.remove();
    }
}
