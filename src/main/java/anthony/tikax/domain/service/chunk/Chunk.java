package anthony.tikax.domain.service.chunk;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Chunk {

    private String chunkId;        // 全局唯一标识
    private String fileMd5;      // 关联文件
    private Integer chunkIndex;      // 分块序号
    private String chunkText;    // chunk文本
    private Integer chunkSize;       // 文本大小（字节）
    private LocalDateTime createdAt;

}
