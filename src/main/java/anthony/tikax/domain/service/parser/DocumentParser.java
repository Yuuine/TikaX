package anthony.tikax.domain.service.parser;

import anthony.tikax.domain.model.FileProcessingContext;

public interface DocumentParser {

    /**
     * 当前解析器支持的 MIME 类型，如 application/pdf
     */
    String supportedMimeType();

    /**
     * 解析文件内容，返回纯文本
     */
    String parse(FileProcessingContext ctx);
}
