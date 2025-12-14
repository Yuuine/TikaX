package anthony.tikax.domain.service.chunk.impl;

import anthony.tikax.domain.service.chunk.Chunk;
import anthony.tikax.domain.service.chunk.TextChunker;
import anthony.tikax.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlidingWindowTextChunker implements TextChunker {

    /**
     * 单个 chunk 最大字符数
     */
    private static final int CHUNK_SIZE = 500;

    /**
     * 相邻 chunk 的重叠字符数
     */
    private static final int OVERLAP = 50;

    @Override
    public List<Chunk> chunk(String text, String fileMd5) {

        List<Chunk> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        int textLength = text.length();
        int start = 0;
        int index = 0;

        while (true) {

            int end = Math.min(start + CHUNK_SIZE, textLength);

            // 尝试在自然边界截断
            end = adjustToNaturalBoundary(text, start, end);

            String chunkText = text.substring(start, end).trim();
            if (!chunkText.isEmpty()) {
                Chunk chunk = new Chunk();
                String chunkId = UUIDUtil.UUIDGenerate();
                chunk.setChunkId(chunkId);
                chunk.setFileMd5(fileMd5);
                chunk.setChunkIndex(index++);
                chunk.setChunkText(chunkText);
                chunk.setChunkSize(chunkText.getBytes(StandardCharsets.UTF_8).length);
                chunk.setCreatedAt(LocalDateTime.now());
                chunks.add(chunk);
            }

            if (end >= textLength) {
                break;
            }

            // 滑动窗口：回退 overlap
            start = end - OVERLAP;

            if (start <= 0) {
                start = end;
            }
        }

        return chunks;
    }

    /**
     * 在句号 / 换行等自然边界处截断，避免语义被强行切断
     */
    private int adjustToNaturalBoundary(String text, int start, int end) {
        for (int i = end; i > start; i--) {
            char c = text.charAt(i - 1);
            if (c == '\n' || c == '。' || c == '.' || c == '！' || c == '？') {
                return i;
            }
        }
        return end;
    }
}

