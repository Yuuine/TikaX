package anthony.tikax.domain.service.chunk;

import java.util.List;

public interface TextChunker {

    List<Chunk> chunk(String text, String fileMd5);

}
