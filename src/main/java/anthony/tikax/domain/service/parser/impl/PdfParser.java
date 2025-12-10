package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 最简版 PDF 解析器：直接用 Tika 抽取文本，不做任何类型检测
 * 企业级特点：稳定、日志清晰、可观测、异常统一处理
 */
@Slf4j
@Component
public class PdfParser implements DocumentParser {

    @Override
    public String supportedMimeType() {
        return "application/pdf";
    }

    @Override
    public String parse(FileProcessingContext ctx) {
        byte[] content = ctx.getContent();
        if (content == null || content.length == 0) {
            throw new BizException(ErrorCode.FILE_EMPTY);
        }

        String fileName = ctx.getFileName();
        String md5 = ctx.getFileMd5();

        log.info("开始解析 PDF: name={}, size={} bytes, md5={}", fileName, content.length, md5);

        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            long start = System.currentTimeMillis();

            BodyContentHandler handler = new BodyContentHandler(-1); // 不限制长度
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            // 配置：保证文本顺序（阅读顺序最重要）
            PDFParserConfig config = new PDFParserConfig();
            config.setSortByPosition(true);
            context.set(PDFParserConfig.class, config);

            new AutoDetectParser().parse(is, handler, metadata, context);

            String text = handler.toString().trim();
            long cost = System.currentTimeMillis() - start;

            log.info("PDF 解析完成，耗时 {} ms，文本长度 {} 字符", cost, text.length());

            return text;
        } catch (Exception e) {
            log.error("PDF 解析失败: name={}, md5={}, error={}", fileName, md5, e.getMessage(), e);
            throw new BizException(ErrorCode.FILE_PARSE_ERROR, e);
        }
    }
}