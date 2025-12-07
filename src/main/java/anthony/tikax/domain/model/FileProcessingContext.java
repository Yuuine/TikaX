package anthony.tikax.domain.model;

import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Data
public class FileProcessingContext {

    private String fileMd5;
    private String fileName;
    private Long totalSize;
    private String extension;
    private String mimeType;
    private byte[] content; // 复用已读入内存的字节数组

    // 提供一个方便的方法获取新的 InputStream（每次调用都是独立的）
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }
}
