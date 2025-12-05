package anthony.tikax.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileUpload {
    private String fileMd5;
    private String fileName;
    private Long totalSize;
    private String fileType;
    private String extension;
    private String mimeType;
    private String plainText;
    private Integer status;
    private String userId;
    private LocalDateTime createAt;

}
