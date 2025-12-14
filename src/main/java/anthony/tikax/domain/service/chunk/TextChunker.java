package anthony.tikax.domain.service.chunk;

import java.util.function.Consumer;

public interface TextChunker {

    /**
     * 流式 chunk 文本，每生成一个 chunk 就通过 consumer 处理。
     *
     * @param text 文本内容
     * @param fileMd5 文件 MD5
     * @param chunkConsumer 每个生成的 Chunk 的处理逻辑
     */
    void chunkStream(String text, String fileMd5, Consumer<Chunk> chunkConsumer);

}
