package anthony.tikax.domain.service.chunk;

import anthony.tikax.domain.service.chunk.impl.LangChain4jRecursiveTextChunker;
import anthony.tikax.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChunkService {

    private final LangChain4jRecursiveTextChunker langChain4jRecursiveTextChunker;
    private final FileMapper fileMapper;

    public void processAndSave(String text, String fileMd5) {
        if (text == null || text.isBlank()) {
            return;
        }

        // 批量缓存大小，避免每个 chunk 都单独插库
        int batchSize = 100;
        var batch = new java.util.ArrayList<Chunk>(batchSize);

        langChain4jRecursiveTextChunker.chunkStream(text, fileMd5, chunk -> {
            batch.add(chunk);
            if (batch.size() >= batchSize) {
                fileMapper.batchInsert(batch);
                batch.clear();
            }
        });

        // 插入剩余的 chunk
        if (!batch.isEmpty()) {
            fileMapper.batchInsert(batch);
        }

        log.info("Finished chunking and saving document: {}", fileMd5);
    }
}
