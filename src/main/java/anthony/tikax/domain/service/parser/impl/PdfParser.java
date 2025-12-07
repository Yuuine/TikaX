package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;

/**
 * PDF解析器
 */
public class PdfParser implements DocumentParser {

    @Override
    public String supportedMimeType() {
        return "application/pdf";
    }

    /**
     * 解析PDF文件
     *
     * @param ctx 文件处理上下文
     * @return 解析后的文本内容
     */
    @Override
    public String parse(FileProcessingContext ctx) {

        return "pdf 文本内容";
    }
}
