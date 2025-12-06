package anthony.tikax.domain.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaInfo {

    private String baseName;//不含扩展名的文件名
    private Long totalSize;//文件大小
    private String fileType;//文件类型
    private String extension;//扩展名
    private String mimeType;//MIME类型
}
