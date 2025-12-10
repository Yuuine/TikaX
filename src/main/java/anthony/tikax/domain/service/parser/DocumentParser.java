package anthony.tikax.domain.service.parser;

import anthony.tikax.domain.model.FileProcessingContext;

import java.util.List;

public interface DocumentParser {

    /**
     * 前解析器支持的多个 MIME 类型
     */
    List<String> supportedMimeTypes();;

    /**
     * 解析文件内容，返回纯文本
     */
    String parse(FileProcessingContext ctx);
}
