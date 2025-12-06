package anthony.tikax.domain.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
public class UploadFileDO {

    private String fileMd5;
    private String fileName;
    private Integer totalSize;
    private String fileType;
    private String extension;
    private String mimeType;
    private Integer status;
    private Integer userId;
    private LocalDateTime createAt;
}
