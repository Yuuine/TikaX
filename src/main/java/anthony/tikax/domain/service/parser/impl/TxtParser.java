package anthony.tikax.domain.service.parser.impl;

import anthony.tikax.domain.model.FileProcessingContext;
import anthony.tikax.domain.service.parser.DocumentParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class TxtParser implements DocumentParser {
    @Override
    public List<String> supportedMimeTypes() {
        // 常见 txt 类型
        return List.of("text/plain");
    }

    @Override
    public String parse(FileProcessingContext ctx) {

        log.info("[TxtParser] 开始解析 txt 文件");

        byte[] bytes = ctx.getContent();

        // 基础 TXT 解码（UTF-8）
        String text = new String(bytes, StandardCharsets.UTF_8);

        // 基础清洗处理（可扩展）
        // - 去掉 BOM
        // - 正规化换行符
        // - 去掉多余空行（如果你希望保持原始语义，可不处理）

        text = normalize(text);

        log.info("[TxtParser] 解析完成，文本长度 {} 字节", text.length());

        return text;
    }

    private String normalize(String text) {
        if (text.startsWith("\uFEFF")) {
            text = text.substring(1);
        }

        // 统一换行符
        text = text.replace("\r\n", "\n").replace("\r", "\n");

        return text;
    }
}