package anthony.tikax.domain.service.chunk.impl;

import anthony.tikax.domain.service.chunk.Chunk;
import anthony.tikax.domain.service.chunk.TextChunker;
import anthony.tikax.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * !!!注意：手动分块效果非常之差，已废弃。建议使用 LangChain4jRecursiveTextChunker
 * 使用滑动窗口分块器实现的 TextChunker。
 */
@Service
public class SlidingWindowTextChunker implements TextChunker {

    /**
     * 单个 chunk 目标字符数（最大）
     */
    private static final int CHUNK_SIZE = 300;

    /**
     * 相邻 chunk 的固定重叠字符数
     */
    private static final int OVERLAP = 50;

    /**
     * 流式生成 chunk，通过 consumer 处理
     */
    @Override
    public void chunkStream(String text, String fileMd5, Consumer<Chunk> chunkConsumer) {
        if (text == null || text.isBlank()) {
            return;
        }

        int textLength = text.length();
        int start = 0;
        int index = 0;

        while (start < textLength) {
            int idealEnd = Math.min(start + CHUNK_SIZE, textLength);

            // 在 [idealEnd - (CHUNK_SIZE - OVERLAP), idealEnd] 范围内寻找最后一个自然边界
            int adjustedEnd = adjustToNaturalBoundary(text, start, idealEnd);

            // 如果调整后端点太靠近 start（可能导致重叠不足或 chunk 过短），则使用 idealEnd
            if (adjustedEnd - start < CHUNK_SIZE - OVERLAP) {
                adjustedEnd = idealEnd;
            }

            String chunkText = text.substring(start, adjustedEnd);
            // 可选：仅去除头部空白，保留段落尾部换行等格式
            // chunkText = chunkText.stripLeading();

            if (!chunkText.isBlank()) {
                Chunk chunk = new Chunk();
                chunk.setChunkId(UUIDUtil.UUIDGenerate());
                chunk.setFileMd5(fileMd5);
                chunk.setChunkIndex(index++);
                chunk.setChunkText(chunkText);
                chunk.setChunkSize(chunkText.length());
                chunk.setCreatedAt(LocalDateTime.now());

                chunkConsumer.accept(chunk);
            }

            // 固定步长：前进 CHUNK_SIZE - OVERLAP
            start += (CHUNK_SIZE - OVERLAP);
            // 防止越界（文本剩余部分不足 CHUNK_SIZE 时）
            if (start >= textLength) {
                break;
            }
        }
    }

    /**
     * 在指定范围内寻找最后一个自然句子边界
     * 搜索范围：从 idealEnd 往前，直到 start + (CHUNK_SIZE - OVERLAP)
     * 以保证即使截断也有足够的固定重叠
     */
    private int adjustToNaturalBoundary(String text, int start, int idealEnd) {
        int minEnd = start + (CHUNK_SIZE - OVERLAP);

        // 扩展边界字符集，覆盖中英文常见句末标点及换行
        String boundaries = "\n。.!?！？";

        for (int i = idealEnd; i > minEnd; i--) {
            char c = text.charAt(i - 1);
            // 检查当前字符是否为边界，或其后是否紧跟换行/空格（处理 ". " 等情况）
            if (boundaries.indexOf(c) != -1) {
                return i; // 在边界字符之后截断，使其包含在当前 chunk 中
            }
        }
        // 未找到合适边界，返回 idealEnd（强制接近目标大小）
        return idealEnd;
    }
}