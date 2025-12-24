package anthony.tikax.domain.service.chunk.impl;

import anthony.tikax.config.ChunkerProperties;
import anthony.tikax.domain.service.chunk.Chunk;
import anthony.tikax.domain.service.chunk.TextChunker;
import anthony.tikax.utils.UUIDUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * 使用 LangChain4j 递归分块器实现的 TextChunker。
 */
@Service
public class LangChain4jRecursiveTextChunker implements TextChunker {

    private final ChunkerProperties properties;

    @Autowired
    public LangChain4jRecursiveTextChunker(ChunkerProperties properties) {
        this.properties = properties;
    }

    /**
     * 流式生成 chunk，通过 consumer 处理。
     */
    @Override
    public void chunkStream(String text, String fileMd5, Consumer<Chunk> chunkConsumer) {
        if (text == null || text.isBlank()) {
            return;
        }

        // 创建 LangChain4j Document
        Document document = Document.from(text);

        // 使用递归分块器：优先按 \n\n、\n、句子边界等自然分隔符分割
        var splitter = DocumentSplitters.recursive(properties.getChunkSize(), properties.getOverlap());


        List<TextSegment> segments = splitter.split(document);

        int index = 0;
        for (TextSegment segment : segments) {
            String chunkText = segment.text().trim();

            if (chunkText.isEmpty()) {
                continue;
            }

            Chunk chunk = new Chunk();
            chunk.setChunkId(UUIDUtil.UUIDGenerate());
            chunk.setFileMd5(fileMd5);
            chunk.setChunkIndex(index++);
            chunk.setChunkText(chunkText);
            chunk.setChunkSize(chunkText.length()); // 使用 length() 与原有实现保持一致
            chunk.setCreatedAt(LocalDateTime.now());

            chunkConsumer.accept(chunk);
        }
    }
}