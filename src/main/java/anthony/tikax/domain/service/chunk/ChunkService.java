package anthony.tikax.domain.service.chunk;


import anthony.tikax.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChunkService {

    private final TextChunker textChunker;
    private final FileMapper fileMapper;

    public void processAndSave(String text, String fileMd5) {
        List<Chunk> chunks = textChunker.chunk(text, fileMd5);
        if (chunks.isEmpty()) {
            return;
        }
        Integer result = fileMapper.batchInsert(chunks);
        log.info("Inserted {} chunks into database", result);
    }

}
